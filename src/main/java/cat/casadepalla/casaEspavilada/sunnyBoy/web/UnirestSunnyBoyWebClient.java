/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.json.JSONObject;

/**
 *
 * @author gabalca
 */
public class UnirestSunnyBoyWebClient implements SunnyBoyWebClient{
    
    private SunnyBoyConfigResolver data;
    private SunnyBoyWebSessionManager sessionManager;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    @Inject
    public UnirestSunnyBoyWebClient(SunnyBoyConfigResolver data, SunnyBoyWebSessionManager sessionManager) {
        if(data==null || sessionManager==null){
            throw new IllegalArgumentException("dataFactory and sessionManager must not be null");
        }
        this.data = data;
        this.sessionManager = sessionManager;
    }

    private String buildJSONArrayWithCodes(String... codes){
        if(codes==null) return "[]";
        StringBuilder sb=new StringBuilder();  
        for(String code:codes){
            if(sb.length()!=0) sb.append(",");
            sb.append("\"").append(data.getDataCode(code)).append("\"");
        }
        return sb.insert(0, "[").append("]").toString();
    }
    
    private HttpResponse<JsonNode> performRequest(String... codes) throws UnirestException, SunnyBoyWebClientException{
        return Unirest.post(data.getDataUrl())
                        .queryString(data.getSessionParam(), sessionManager.getSessionToken())
                        .header("accept", "application/json")
                        .body("{\"destDev\":[],\"keys\":"+buildJSONArrayWithCodes(codes)+"}")
                        .asJson();
    }
    
    private void throwSunnyBoyExceptionWithMessageAndBody(String message, HttpResponse<JsonNode> jsonResponse) throws SunnyBoyWebClientException{
        throw new SunnyBoyWebClientException(
                                        message
                                                +", body: "
                                                +jsonResponse.getBody(),
                                        jsonResponse.getBody().toString());
        
    }
    
    @Override
    public Map<String,String> getValues(String... codes) throws SunnyBoyWebClientException {
        
        try {
            logger.log(Level.INFO,"Performing the request to get all codes {0} ",codes);
            HttpResponse<JsonNode> jsonResponse=performRequest(codes);

            if(jsonResponse.getStatus()==200){
                logger.log(Level.INFO,"Got a 200 response! ");
                JsonNode body = jsonResponse.getBody();
                //System.out.println("RESPONSE: "+body);
                JSONObject object = body.getObject();
                if(object.has("err")){
                    int errorCode=object.getInt("err");
                    if(errorCode==401){
                        logger.log(Level.INFO,"The session has expired. Refreshing token.");
                        sessionManager.refreshToken();
                        logger.log(Level.INFO,"Performing the request again.");
                        jsonResponse=performRequest(codes);
                        object = jsonResponse.getBody().getObject();
                    }
                }
                if(object.has("result")){
                    JSONObject jsonObject = object.getJSONObject("result");
                    
                    if(jsonObject.keys()!=null && jsonObject.keys().hasNext()){
                        JSONObject dataObject=jsonObject.getJSONObject(jsonObject.keys().next());
                        Map<String,String> result=new HashMap<>(codes.length);
                        for(String codeHumanReadable:codes){
                            String code=data.getDataCode(codeHumanReadable);
                            if(dataObject.has(code)){
                                JSONObject codeObject=dataObject.getJSONObject(code);
                                JSONObject arrayElement=codeObject.getJSONArray("1").getJSONObject(0);
                                if(arrayElement.has("val")){
                                    if(arrayElement.isNull("val")){
                                        result.put(codeHumanReadable, null);
                                    }else{
                                        result.put(codeHumanReadable, String.valueOf(arrayElement.get("val")));
                                    }
                                }else{
                                    logger.log(Level.SEVERE,"Val element for code "
                                            + "{0} not present in the response: {1} ",new Object[]{code,jsonResponse});
                                    throwSunnyBoyExceptionWithMessageAndBody(
                                        "Val element not present in the response for code "+code+"! "
                                                ,jsonResponse);
                                }
                            }else{
                                logger.log(Level.SEVERE,"Code {0} not present in the response: {1} ",new Object[]{code,jsonResponse});
                                throwSunnyBoyExceptionWithMessageAndBody(
                                        "Code "+code+" not present in the response! "
                                                ,jsonResponse);
                            }
                        }
                        return result;
                    }else{
                        logger.log(Level.SEVERE,"Empty result object in the response: {0} ",jsonResponse);
                        throwSunnyBoyExceptionWithMessageAndBody(
                                        "Empty result in the response"
                                                ,jsonResponse);
                        
                    }               
                  }else{
                    logger.log(Level.SEVERE,"No result object in the response: {0} ",jsonResponse);
                    throwSunnyBoyExceptionWithMessageAndBody(
                                        "No result in the response"
                                                ,jsonResponse);
                }
            }else{
                logger.log(Level.SEVERE,"Got a non 200 response: {0} ",jsonResponse.getStatus());
                throwSunnyBoyExceptionWithMessageAndBody(
                                        "No success response code:"
                                +jsonResponse.getStatus()
                                                ,jsonResponse);
                
            }
            return null;
        } catch (UnirestException ex) {
            logger.log(Level.SEVERE,"Got a connection exception "+ex.getMessage(),ex);
            throw new SunnyBoyWebClientException("Unitrest exception when getting data",ex);
        }        
    }
    
}

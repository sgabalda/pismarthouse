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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.json.JSONObject;

/**
 *
 * @author gabalca
 */
public class UnirestSunnyBoyWebSessionManager implements SunnyBoyWebSessionManager{
    
    private String token;
    
    private SunnyBoyConfigResolver dataFactory;
    private SunnyBoyWebSessionValidStrategy timeoutStrategy;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    @Inject
    public UnirestSunnyBoyWebSessionManager(SunnyBoyConfigResolver dataFactory,
            SunnyBoyWebSessionValidStrategy timeoutStrategy) {
        if(dataFactory==null || timeoutStrategy==null){
            throw new IllegalArgumentException("dataFactory and timeoutStrategy must not be null");
        }
        this.timeoutStrategy=timeoutStrategy;
        this.dataFactory = dataFactory;
    }
    

    @Override
    public final String getSessionToken() throws SunnyBoyWebClientException {
        logger.info("Getting the session token");
        if(timeoutStrategy.isTokenValid()){
            logger.info("The token has not reached timout. Reusing it");
            return token;
        }
        else{
            logger.info("The token has reached timout. Refreshing it");
            refreshToken();
            return token;
        }
    }

    @Override
    public final void refreshToken() throws SunnyBoyWebClientException {
        try {
            logger.info("Sending request to refresh token");
                HttpResponse<JsonNode> jsonResponse = Unirest.post(dataFactory.getLoginURL())
                        .header("accept", "application/json")
                        .body("{\"right\":\""+dataFactory.getUsername()+"\","
                                + "\"pass\":\""+dataFactory.getPassword()+"\"}")
                        .asJson();
                if(jsonResponse.getStatus()==200){
                    JsonNode body = jsonResponse.getBody();
                    System.out.println("RESPONSE: "+body);
                    JSONObject object = body.getObject();
                    JSONObject jsonObject = object.getJSONObject("result");
                    if(jsonObject.has("sid") && !jsonObject.isNull("sid")){
                        token=jsonObject.getString("sid");
                        timeoutStrategy.tokenUpdated();
                        logger.info("Got the token and updated it");
                    }else{
                        logger.log(Level.SEVERE,"The response does not have a SID."
                                + " SOmething went wrong {0} ",jsonResponse);
                        throw new SunnyBoyWebClientException(
                            "Could not get SID. Wrong username/password?",jsonResponse.getBody().toString());
                    }
                }else{
                    logger.log(Level.SEVERE,"Could not authenticate. "
                                + " SOmething went wrong {0} ",jsonResponse);
                    throw new SunnyBoyWebClientException(
                            "Could not authenticate: Status:"
                                    +jsonResponse.getStatus()+", body: "
                                    +jsonResponse.getBody(),
                            jsonResponse.getBody().toString()
                    );
                }

            } catch (UnirestException ex) {
                logger.log(Level.SEVERE,"Got a connection exception "+ex.getMessage(),ex);
                throw new SunnyBoyWebClientException(ex);
            }
    }
    
}

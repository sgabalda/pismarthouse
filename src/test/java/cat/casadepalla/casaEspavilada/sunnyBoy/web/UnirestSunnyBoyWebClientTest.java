/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import com.mashape.unirest.http.Unirest;
import io.specto.hoverfly.junit.core.Hoverfly;
import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.HttpBodyConverter.jsonWithSingleQuotes;
import io.specto.hoverfly.junit.dsl.ResponseBuilder;
import io.specto.hoverfly.junit.dsl.ResponseCreators;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import io.specto.hoverfly.junit5.HoverflyExtension;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.*;

/**
 *
 * @author gabalca
 */
@ExtendWith(HoverflyExtension.class)
public class UnirestSunnyBoyWebClientTest {

       
    public UnirestSunnyBoyWebClientTest() {
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
        Unirest.setHttpClient(httpClient);
    }
    
    private final static String DATA_URL="http://192.168.12.3/dyn/getValues.json";
    public static final String SERVICE_HOST="192.168.12.3";
    public static final String RESOURCE_PATH_LOGIN="/dyn/getValues.json";
    private final static String DATA="ProducedPower";
    private final static String DATA_CODE="6100_40263F00";
    
    public static final String SESSION_ID="2a95do-OJYlOYr-V";
    public static final String SESSION_PARAM="sid";
    
    private UnirestSunnyBoyWebClient client;
    private SunnyBoyConfigResolver data;
    private SunnyBoyWebSessionManager sessionManager;
    
    @BeforeEach
    public void setUp() {
        data=mock(SunnyBoyConfigResolver.class);
        when(data.getDataUrl()).thenReturn(DATA_URL);
        when(data.getSessionParam()).thenReturn(SESSION_PARAM);
        when(data.getDataCode(DATA)).thenReturn(DATA_CODE);
        sessionManager=mock(SunnyBoyWebSessionManager.class);
        client=new UnirestSunnyBoyWebClient(data,sessionManager);
        client.addLogger(mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getValues method, of class UnirestSunnyBoyWebClient.
     */
    @Test
    public void testGetValues() throws Exception {
    }

    @Test
    public void testGetValuesGetsSessionTokenFromSessionManager(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{\"val\":null}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        client.getValues(DATA);
        
        verify(sessionManager).getSessionToken();
        verify(sessionManager,never()).refreshToken();
        
    }
    @Test
    public void testGetValuesRefreshesTokenIf401(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"err\":401}"
            )).andSetState("got", "true"))
            .post(RESOURCE_PATH_LOGIN)
            .withState("got", "true")
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{\"val\":null}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        client.getValues(DATA);
        
        verify(sessionManager).refreshToken();
        
    }
    
    @Test
    public void testPropagatesExceptionIfExceptionOnGetToken(Hoverfly hoverfly) throws Exception {
        when(sessionManager.getSessionToken()).thenThrow(new SunnyBoyWebClientException("test","test"));
        Executable ex=()->{
            client.getValues(DATA);
        };
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient must propagate exception on getToken");

    }
    @Test
    public void testPropagatesExceptionIfExceptionOnRefreshToken(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"err\":401}"
            )))));
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        //the session id is available, but returns a 401
        doThrow(new SunnyBoyWebClientException("test","test")).when(sessionManager).refreshToken();
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient must propagate exception on refreshToken");
    }
    
    @Test
    public void testThrowsExceptionIfResponseNot200(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(ResponseCreators.notFound().body(jsonWithSingleQuotes(
                            "{\"err\":401}"
            )))));
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient musth raise exception if not 200 response");
    }
    
    @Test
    public void testThrowsExceptionIfResponseHasNoResult(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result2\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{\"val\":null}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient musth raise exception if not result object in the response");
    }
    
    @Test
    public void testReturnsDataValueIfCodeAndDataIsInResponse(Hoverfly hoverfly) throws Exception {
        String simulatedData="247";
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{\"val\":"+simulatedData+"}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Map<String,String> result=client.getValues(DATA);
        assertEquals(simulatedData,result.get(DATA));
    }
    
    @Test
    public void testReturnsNullValueIfCodeIsInResponseButNull(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{\"val\":null}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Map<String,String> result=client.getValues(DATA);
        assertNull(result.get(DATA_CODE));
    }
    
    @Test
    public void testThrowsExceptionIfemptyResultObjectInResponse(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient musth raise exception if empty result object in the response");
    }
    
        @Test
    public void testThrowsExceptionIfNotValForCodeInResponse(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+"\":{"
                                    + "\"1\":[{}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient musth raise exception if no 'val' result object in the response");
    }
    
    @Test
    public void testThrowsExceptionIfCodeNotInResponse(Hoverfly hoverfly) throws Exception {
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN)
            .queryParam(SESSION_PARAM, SESSION_ID)
            .body("{\"destDev\":[],\"keys\":[\""+DATA_CODE+"\"]}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{\"result\":{\"0156-76BCE994\":{"
                                    + "\""+DATA_CODE+22+"\":{"
                                    + "\"1\":[{\"val\":null}]}}}}"
            )))
        )); 
        when(sessionManager.getSessionToken()).thenReturn(SESSION_ID);
        
        Executable ex=()->{
            client.getValues(DATA);
        };
        
        assertThrows(SunnyBoyWebClientException.class, ex,
                "UnirestSunnyBoyWebClient musth raise exception if code not in the response");
    }
    
    @Test
    public void testCanNotBeInitializedWithoutSessionManager(Hoverfly hoverfly) throws Exception {
        Executable ex=()->{
            new UnirestSunnyBoyWebClient(data, null);
        };
        assertThrows(IllegalArgumentException.class, ex,"UnirestSunnyBoyWebClient needs a non null sessionManager");
    
    }
    @Test
    public void testCanNotBeInitializedWithoutData(Hoverfly hoverfly) throws Exception {
        Executable ex=()->{
            new UnirestSunnyBoyWebClient(null, sessionManager);
        };
        assertThrows(IllegalArgumentException.class, ex,"UnirestSunnyBoyWebClient needs a non null data");
    
    }

}

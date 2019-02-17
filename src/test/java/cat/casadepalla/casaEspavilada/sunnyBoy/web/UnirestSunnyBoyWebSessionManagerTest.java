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
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.verification.HoverflyVerifications.times;
import io.specto.hoverfly.junit5.HoverflyExtension;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 *
 * @author gabalca
 */
@ExtendWith(HoverflyExtension.class)
public class UnirestSunnyBoyWebSessionManagerTest {
    
    public static final String RIGHT_USER="usr";
    public static final String RIGHT_PASS="passFake";
    public static final String BAD_PASS="bad-password";
    public static final String SUCCESS_ID="2a95do-OJYlOYr-V";
    public static final String LOGIN_URL="http://192.168.12.3/dyn/login.json";
    public static final String SERVICE_HOST="192.168.12.3";
    public static final String RESOURCE_PATH_LOGIN="/dyn/login.json";
    
    private UnirestSunnyBoyWebSessionManager sutTokenValid;
    private UnirestSunnyBoyWebSessionManager sutTokenNotValid;
    private SunnyBoyConfigResolver data;
    private SunnyBoyWebSessionValidStrategy timeoutTokenValid;
    private SunnyBoyWebSessionValidStrategy timeoutTokenInvalid;
    
    public UnirestSunnyBoyWebSessionManagerTest() {
        HttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
        Unirest.setHttpClient(httpClient);

    }
    
    @BeforeEach
    public void setUp() {
        
        data=mock(SunnyBoyConfigResolver.class);
        when(data.getLoginURL()).thenReturn(LOGIN_URL);
        when(data.getUsername()).thenReturn(RIGHT_USER); 
        
        timeoutTokenInvalid=mock(SunnyBoyWebSessionValidStrategy.class);
        when(timeoutTokenInvalid.isTokenValid()).thenReturn(false);
        
        timeoutTokenValid=mock(SunnyBoyWebSessionValidStrategy.class);
        when(timeoutTokenValid.isTokenValid()).thenReturn(true);
        
        sutTokenValid=new UnirestSunnyBoyWebSessionManager( data,timeoutTokenValid );
        sutTokenNotValid=new UnirestSunnyBoyWebSessionManager( data,timeoutTokenInvalid);
        
        sutTokenValid.addLogger(mock(Logger.class));
        sutTokenNotValid.addLogger(mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetSessionTokenReturnsATokenIfNotValid(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(RIGHT_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        assertEquals(SUCCESS_ID,sutTokenNotValid.getSessionToken());
    }
    
    @Test
    public void testGetSessionTokenPerformsACallToWSIfNotValid(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(RIGHT_PASS); 
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        sutTokenNotValid.getSessionToken();
        
        hoverfly.verify(service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).anyBody(),times(1));
    }
    
    @Test
    public void testGetSessionTokenReturnsATokenValidWithoutCall(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(RIGHT_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        sutTokenValid.refreshToken();
        hoverfly.verify(service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).anyBody(),times(1));
        assertEquals(SUCCESS_ID,sutTokenValid.getSessionToken());
        hoverfly.verify(service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).anyBody(),times(1));
    }
    
    @Test
    public void testRefreshSessionFailsIfBadPass(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(BAD_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+BAD_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':null}}"
            )))
        )); 
        Executable ex=()->{
           sutTokenValid.refreshToken();
        };
        SunnyBoyWebClientException exc=assertThrows(SunnyBoyWebClientException.class, ex,"Must fail if password or username incorrect"); 
        assertTrue(exc.getMessage().contains("username"),"The message contains username");
        assertTrue(exc.getMessage().contains("password"),"The message contains password");
    }
    
    @Test
    public void testGetTokenFailsIfBadPassAndTokenNotValid(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(BAD_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+BAD_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':null}}"
            )))
        )); 
        Executable ex=()->{
           sutTokenNotValid.getSessionToken();
        };
        SunnyBoyWebClientException exc=assertThrows(SunnyBoyWebClientException.class, ex,"Must fail if password or username incorrect"); 
        assertTrue(exc.getMessage().contains("username"),"The message contains username");
        assertTrue(exc.getMessage().contains("password"),"The message contains password");
    }


    /**
     * Test of refreshToken method, of class UnirestSunnyBoyWebSessionManager.
     */
    @Test
    public void testRefreshToken(Hoverfly hoverfly) throws Exception {
        when(data.getPassword()).thenReturn(RIGHT_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        sutTokenValid.refreshToken();
        hoverfly.verify(service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).anyBody(),times(1));
    }
    
    /**
     * Test of refreshToken method, of class UnirestSunnyBoyWebSessionManager.
     */
    @Test
    public void testRefreshTokenNotifiesTokenUpdatedToTimeout(Hoverfly hoverfly) throws Exception {
        when(data.getPassword()).thenReturn(RIGHT_PASS);   
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        sutTokenValid.refreshToken();
        verify(timeoutTokenValid).tokenUpdated();
    }

    @Test
    public void testGetSessionTokenNotifiesTokenUpdatedIfNotValid(Hoverfly hoverfly) throws Exception {
        
        when(data.getPassword()).thenReturn(RIGHT_PASS); 
        
        hoverfly.simulate(dsl(
        service(SERVICE_HOST)
            .post(RESOURCE_PATH_LOGIN).body("{\"right\":\""+RIGHT_USER+"\",\"pass\":\""+RIGHT_PASS+"\"}")
            .willReturn(success().body(jsonWithSingleQuotes(
                            "{'result':{'sid':'"+SUCCESS_ID+"'}}"
            )))
        ));   
        
        sutTokenNotValid.getSessionToken();
        
        verify(timeoutTokenInvalid).tokenUpdated();
    }
    
    @Test
    public void testCanNotBeInitializedWithoutSessionManager(Hoverfly hoverfly) throws Exception {
        Executable ex=()->{
            new UnirestSunnyBoyWebSessionManager(data, null);
        };
        assertThrows(IllegalArgumentException.class, ex,"UnirestSunnyBoyWebSessionManager needs a non null Session manager");
    
    }
    
    @Test
    public void testCanNotBeInitializedWithoutData(Hoverfly hoverfly) throws Exception {
        Executable ex=()->{
            new UnirestSunnyBoyWebSessionManager(null, timeoutTokenInvalid);
        };
        assertThrows(IllegalArgumentException.class, ex,"UnirestSunnyBoyWebSessionManager needs a non null data");
    
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author gabalca
 */
public class FixedTimeoutSunnyBoyWebSessionValidStrategyTest {
    
    public FixedTimeoutSunnyBoyWebSessionValidStrategyTest() {
    }
    
    private FixedTimeoutSunnyBoyWebSessionValidStrategy sut;
    private Clock clock;
    private final int defaultOffsetMinutes=60;
    
    @BeforeEach
    public void setUp() {
        clock=Mockito.mock(Clock.class);
        sut=new FixedTimeoutSunnyBoyWebSessionValidStrategy(clock,defaultOffsetMinutes);
        sut.addLogger(Mockito.mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testIsTokenInvalidIfNeverUpdated() {
        assertFalse(sut.isTokenValid());        
    }
    @Test
    public void testIsTokenValidIfLessThanTimeout() {
        
        //when have passed less than 10 minutes
        Instant first = Instant.now();                  
        Instant second = first.plusSeconds(234); 
        when(clock.instant()).thenReturn(first, second);
        //then token must be valid
        sut.tokenUpdated();
        assertTrue(sut.isTokenValid(), "Token must be valid if less than timeout");
    }
    
    @Test
    public void testIsTokenInValidIfMoreThanTimeout() {
        //when have passed more than 10 minutes
        Instant first = Instant.now();                  
        Instant second = first.plus(defaultOffsetMinutes+1,ChronoUnit.MINUTES); 
        when(clock.instant()).thenReturn(first, second);
        //then token must be NOT valid
        sut.tokenUpdated();
        assertFalse(sut.isTokenValid(),"Token must be invalid if more than timeout");
    }

    /**
     * Test of tokenUpdated method, of class FixedTimeoutSunnyBoyWebSessionValidStrategy.
     */
    @Test
    public void testTokenUpdatedUpdatesAtCurrentInstant() {
        Instant first = Instant.now();                  
        Instant second = first.plusSeconds(234); 
        Instant third = first.plus(defaultOffsetMinutes+1,ChronoUnit.MINUTES);
        Instant fourth = first.plus(defaultOffsetMinutes+3,ChronoUnit.MINUTES);
        Instant fifth = first.plus(defaultOffsetMinutes+4,ChronoUnit.MINUTES);
        when(clock.instant()).thenReturn(first, second,third,fourth,fifth);
        sut.tokenUpdated();
        assertTrue(sut.isTokenValid(), "Token must be valid if less than timeout");
        assertFalse(sut.isTokenValid(),"Token must be invalid if more than timeout");
        sut.tokenUpdated();
        assertTrue(sut.isTokenValid(), "Token must be valid if less than timeout");
    }
    
    @Test
    public void testCanNotbeInitializedWithoutClock() throws Exception {
        Executable ex=()->{
            new FixedTimeoutSunnyBoyWebSessionValidStrategy(null, defaultOffsetMinutes);
        };
        assertThrows(IllegalArgumentException.class, ex,"FixedTimoutSunnyBoy Session Manager  needs a non null clock");
    }
    
}

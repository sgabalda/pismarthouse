/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.app.diProviders.ConfigValue;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class FixedTimeoutSunnyBoyWebSessionValidStrategy
implements SunnyBoyWebSessionValidStrategy{
    
    private final Clock clock;
    private final int offsetMinutes;
    private Instant lastUpdated;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    @Inject
    public FixedTimeoutSunnyBoyWebSessionValidStrategy(Clock clock, @ConfigValue(code="sessionTimeoutMinutes") int offsetMinutes) {
        if(clock==null) throw new IllegalArgumentException("Clock can not be null");
        this.clock = clock;
        this.offsetMinutes = offsetMinutes;
    }
    
    

    @Override
    public boolean isTokenValid() {
        logger.info("Checking if token is valid");
        if(lastUpdated==null)return false;
        return clock.instant().isBefore(lastUpdated.plus(offsetMinutes, ChronoUnit.MINUTES));
    }

    @Override
    public void tokenUpdated() {
        logger.info("Updating token");
        lastUpdated=clock.instant();
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.Processor;
import cat.casadepalla.casaEspavilada.core.Sensor;
import cat.casadepalla.casaEspavilada.core.SensorInitializationException;
import cat.casadepalla.casaEspavilada.core.SensorMeasurementException;
import java.time.Clock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class GenericSunnyBoyWebInterfaceSensor implements Sensor {
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}
    
    private SunnyBoyWebClient client;
    private SunnyBoyDataAdapter adapter;
    private Clock clock;
    private SunnyBoyConfigResolver config;
    private long lastMeasuredTime;

    @Inject
    public GenericSunnyBoyWebInterfaceSensor(Clock clock,
            SunnyBoyWebClient client, 
            SunnyBoyDataAdapter adapter,
            SunnyBoyConfigResolver config) {
        
        if(clock==null)throw new IllegalArgumentException("Can not create a "
                + "GenericSunnyBoyWebInterfaceSensor without clock");
        if(config==null)throw new IllegalArgumentException("Can not create a "
                + "GenericSunnyBoyWebInterfaceSensor without data fields");
        if(client==null)throw new IllegalArgumentException("Can not create a "
                + "GenericSunnyBoyWebInterfaceSensor without client");
        if(adapter==null)throw new IllegalArgumentException("Can not create a "
                + "GenericSunnyBoyWebInterfaceSensor without adapter");
        this.client=client;
        this.clock=clock;
        this.config=config;
        this.lastMeasuredTime=0;
        this.adapter=adapter;
    }
    
    @Override
    public void init() throws SensorInitializationException{
        logger.info("Calling init to GenericSunnyBoyWebInterfaceSensor");
    }

    @Override
    public void measure(Processor p) throws SensorMeasurementException{
        logger.info("Going to measure in GenericSunnyBoyWebInterfaceSensor");
        lastMeasuredTime=clock.instant().toEpochMilli();
        try{
            p.process(
                    adapter.getMeasurements(
                            client.getValues(config.getSunnyBoyDataCodesToRead())
                    )
            );
        }catch(SunnyBoyWebClientException | SunnyBoyAdapterException e){
            logger.log(Level.SEVERE,"Exception in measure in "
                    + "GenericSunnyBoyWebInterfaceSensor: "+e.getMessage(),e);
            throw new SensorMeasurementException(e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public long getLastMeasuredTime() {
        return lastMeasuredTime;
    }
    
    
}

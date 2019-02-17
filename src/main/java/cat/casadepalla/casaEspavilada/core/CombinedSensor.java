/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gabalca
 */
public class CombinedSensor implements Sensor {

    private long lastMeasuredTime=0;
    private Sensor[] sensors;
    
    public CombinedSensor(Sensor ... sensors) {
        if(sensors==null || sensors.length==0 || sensors.length==1) {
            throw new IllegalArgumentException("At least two sensors required to create a CombinedSensor");
        }
        this.sensors=new Sensor[sensors.length];
        for(int i=0; i<sensors.length; i++){
            if(sensors[i]==null){
                throw new IllegalArgumentException("Can not combine a null sensor in position "+i);
            }
            this.sensors[i]=sensors[i];
        }
        
    }
    
    

    @Override
    public void init() throws SensorInitializationException {
        for(Sensor s:sensors)
            s.init();
    }

    @Override
    public void measure(Processor p) throws SensorMeasurementException {
        lastMeasuredTime=System.currentTimeMillis();
        for(Sensor s:sensors) s.measure(p);
        
    }

    @Override
    public boolean isReady() {
        for(Sensor s:sensors){
            if(!s.isReady()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public long getLastMeasuredTime() {
        return lastMeasuredTime;
    }
    
}

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
public class CombinedMeasurements implements Measurements{
    
    private final long time;
    private final Map<String,Measure> measures;
    
    public CombinedMeasurements(Map<String,Measure> measures){
        this.time=System.currentTimeMillis();
        this.measures=new HashMap<>(measures);
    }

    @Override
    public Set<String> getMeasurementsNames() {
        Set<String> s=measures.keySet();
        return s;
    }

    @Override
    public long getMeasurementTime() {
        return time;
    }

    @Override
    public Measure getMeasure(String name) {
        if(!measures.containsKey(name)){
            throw new IllegalArgumentException("name "+name+" is not present in this combined measurement");
        }
        return measures.get(name);
    }

    @Override
    public Map<String, Measure> getAllMeasures() {
        return new HashMap<>(measures);
    }
    
}

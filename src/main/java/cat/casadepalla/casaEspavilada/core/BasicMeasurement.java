/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author gabalca
 */
public class BasicMeasurement implements Measurements{

    private final Measure measure;
    private final long measurementTime;
    private final String name;

    public BasicMeasurement(Measure measure,String name) {
        if(name==null){
            throw new IllegalArgumentException("Name must not be null");
        }
        if(measure==null){
            throw new IllegalArgumentException("Measure must not be null");
        }
        this.name=name;
        this.measure=measure;
        this.measurementTime=System.currentTimeMillis();
    }

    @Override
    public long getMeasurementTime() {
        return this.measurementTime;
    }

    @Override
    public Set<String> getMeasurementsNames() {
        Set<String> result=new HashSet<>();
        result.add(name);
        return result;
    }

    @Override
    public Measure getMeasure(String name) {
        if(this.name.equals(name)){
            return measure;
        }
        throw new IllegalArgumentException("Measure name not in this measurement");
    }

    @Override
    public Map<String, Measure> getAllMeasures() {
        Map<String,Measure> result=new HashMap<>();
        result.put(name, measure);
        return result;
    }

    @Override
    public String toString() {
        return "BasicMeasurement{" + "measure=" + measure + ", measurementTime=" + measurementTime + ", name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.measure);
        hash = 83 * hash + (int) (this.measurementTime ^ (this.measurementTime >>> 32));
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BasicMeasurement other = (BasicMeasurement) obj;
        //System.out.println("TImes: "+this.measurementTime+" vs "+other.measurementTime);
        if (this.measurementTime != other.measurementTime) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.measure, other.measure)) {
            return false;
        }
        return true;
    }
    
}

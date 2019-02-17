/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author gabalca
 */
public interface Measurements {
    public Set<String> getMeasurementsNames();
    public long getMeasurementTime();
    public Measure getMeasure(String name);
    public Map<String,Measure> getAllMeasures();
}

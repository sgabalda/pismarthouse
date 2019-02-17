/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

/**
 *
 * @author gabalca
 */
public interface Sensor {
    public void init() throws SensorInitializationException;
    public void measure(Processor p) throws SensorMeasurementException;   //async call to process the measure
    public boolean isReady();
    public long getLastMeasuredTime();
}

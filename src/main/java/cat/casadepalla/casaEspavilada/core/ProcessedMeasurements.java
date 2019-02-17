/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

/**
 *
 * @author gabalca
 * 
 * Measurement that can be modified, keeping the origianl measure.
 * Ready to be used by a chain of processors that consume a resource until is finished
 */
public interface ProcessedMeasurements extends Measurements{
    
    public int getStep();
    public void addStep();
    public Measurements getProcessed();
    
}

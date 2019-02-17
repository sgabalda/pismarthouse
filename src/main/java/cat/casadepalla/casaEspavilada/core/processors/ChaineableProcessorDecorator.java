/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.processors;

import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.ProcessedMeasurements;
import cat.casadepalla.casaEspavilada.core.Processor;

/**
 *
 * @author gabalca
 */
public abstract class ChaineableProcessorDecorator implements Processor{
    
    private Processor next;

    public ChaineableProcessorDecorator(Processor next) {
        if(next==null){
            throw new IllegalArgumentException("Can not create a chainable "
                    + "processor without next in the chain");
        }
        this.next = next;
    }

    @Override
    public final void process(Measurements measures) {
        next.process(processThis(measures));
    }
    
    public abstract ProcessedMeasurements processThis(Measurements me);
    
}

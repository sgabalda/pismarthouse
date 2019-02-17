/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gabalca
 */
public class CombinedProcessor implements Processor{
    
    private Set<Processor> processors;

    public CombinedProcessor(Collection<Processor> processors) {
        if(processors==null || processors.isEmpty()){
            throw new IllegalArgumentException("Can not create a combined Processor with no porcessors");
        }
        this.processors = new HashSet<>(processors);
    }

    @Override
    public void process(Measurements measures) {
        processors.forEach((p) -> {
            p.process(measures);
        });
    }
    
}

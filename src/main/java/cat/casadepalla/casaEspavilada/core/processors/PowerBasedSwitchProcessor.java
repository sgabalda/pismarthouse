/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.processors;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import java.util.logging.Logger;
import javax.inject.Inject;
import cat.casadepalla.casaEspavilada.core.switches.MagnitudeBasedSwitch;

/**
 *
 * @author gabalca
 */
public class PowerBasedSwitchProcessor implements Processor{
    
    private MagnitudeBasedSwitch pbswitch;
    private Measure treshold;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    
    public PowerBasedSwitchProcessor(MagnitudeBasedSwitch pbswitch, Measure treshold) {
        if(pbswitch==null)throw new IllegalArgumentException("Can not create a "
                + "PowerBasedSwitchProcessor without switch");
        if(treshold==null) throw new IllegalArgumentException("Can not create a "
                + "PowerBasedSwitchProcessor without treshold");
        try{
            treshold.compareTo(treshold);
        }catch(Exception e){
            throw new IllegalArgumentException("Can not create a "
                + "PowerBasedSwitchProcessor without camparable treshold");
        }
        this.treshold=treshold;
        this.pbswitch = pbswitch;
    }

    public MagnitudeBasedSwitch getPbswitch() {
        return pbswitch;
    }

    public void setPbswitch(MagnitudeBasedSwitch pbswitch) {
        this.pbswitch = pbswitch;
    }
    
    

    @Override
    public void process(Measurements measures) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

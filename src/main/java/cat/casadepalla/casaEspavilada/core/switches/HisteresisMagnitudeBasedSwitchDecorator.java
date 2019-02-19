/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.switches;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.MeasureUtils;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class HisteresisMagnitudeBasedSwitchDecorator extends MagnitudeBasedSwitchDecorator{
    
    private Measure margin;

    
    public HisteresisMagnitudeBasedSwitchDecorator( Switch decoratedSwitch, Measure treshold,Measure margin) {
        super(decoratedSwitch, treshold);
        if(margin==null)throw new IllegalArgumentException("Can not create a "
                + "HisteresisMagnitudeBasedSwitchDecorator with null margin");
        try{
            treshold.compareTo(margin);
        }catch(RuntimeException e){
            throw new IllegalArgumentException("Can not create a "
                + "HisteresisMagnitudeBasedSwitchDecorator with non compatible"
                + " margin-treholsd pair");
        }
        
        this.margin = margin;
    }

    @Override
    public void setTreshold(Measure m) {
        super.setTreshold(m);
    }

    

    @Override
    public boolean wouldTurnOnWithMeasure(Measure m) {
        if(super.isOn()){
            return super.wouldTurnOnWithMeasure(MeasureUtils.INSTANCE.addMeasures(m,margin));
        }else{
            return super.wouldTurnOnWithMeasure(MeasureUtils.INSTANCE.substractMeasures(m,margin));
        }
    }
    
    
    
    
    
    
}

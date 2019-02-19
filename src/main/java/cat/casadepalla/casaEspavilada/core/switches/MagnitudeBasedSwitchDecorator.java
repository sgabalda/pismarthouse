/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.switches;

import cat.casadepalla.casaEspavilada.core.Measure;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class MagnitudeBasedSwitchDecorator implements MagnitudeBasedSwitch{
    
    private Switch decoratedSwitch;
    private Measure treshold;

    
    public MagnitudeBasedSwitchDecorator(Switch decoratedSwitch, Measure treshold) {
        if(decoratedSwitch==null)throw new IllegalArgumentException("Can not create a "
                + "MagnitudeBasedSwitchDecorator whithout switch");
        this.decoratedSwitch = decoratedSwitch;
        validateTreshold(treshold);
        this.treshold=treshold;
    }
    
    

    @Override
    public Measure getTreshold() {
        return treshold;
    }

    @Override
    public void setTreshold(Measure m) {
        validateTreshold(m);
        this.treshold=m;
    }
    
    private void validateTreshold(Measure m){
        if(m==null) throw new IllegalArgumentException("Can not set a "
                + "null treshold on MagnitudeBasedSwitchDecorator");
        try{
            m.compareTo(m);
        }catch(Exception e){
            throw new IllegalArgumentException("Can not set a "
                + "non camparable treshold on MagnitudeBasedSwitchDecorator");
        }
    }

    @Override
    public void turnOn() {
        decoratedSwitch.turnOn();
    }     

    @Override
    public void turnOff() {
        decoratedSwitch.turnOff();
    }

    @Override
    public boolean isOn() {
        return decoratedSwitch.isOn();
    }

    @Override
    public void turnWithMeasure(Measure m) {
        if(!wouldTurnOnWithMeasure(m)){
            //treshold is higher than measure
            decoratedSwitch.turnOff();
        }else{
            decoratedSwitch.turnOn();
        }
    }

    @Override
    public boolean wouldTurnOnWithMeasure(Measure m) {
        return treshold.compareTo(m) <= 0; //treshold is higher than measure
    }
    
}

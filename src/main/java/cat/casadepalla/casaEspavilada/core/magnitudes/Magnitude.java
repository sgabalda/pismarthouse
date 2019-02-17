/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

import java.util.Arrays;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public enum Magnitude {
    POWER(Unit.W,Unit.kW),
    ENERGY(Unit.Wh,Unit.kWh),
    FREQUENCY(Unit.Hz, Unit.cHz)
    ;
    
    final Unit[] units;
    Magnitude(Unit ... units){
        if(units==null || units.length==0){
            throw new ExceptionInInitializerError("A magnitude must have units!");
        }
        this.units=Arrays.copyOf(units, units.length);
    }
    
    public Unit[] getUnits(){
        return Arrays.copyOf(units, units.length);
    }
    
    public boolean isConvertible(Unit a, Unit b){
        return this.isOfMagnitude(a) && this.isOfMagnitude(b);

    }
    
    public <T> Scalar<T> convert(Scalar<T> scalar,Unit origin, Unit dest){
        if(!isConvertible(origin, dest)){
            throw new IllegalArgumentException("Can not convert from "+origin+" to "+dest+". Both must be units of the "+this+" magnitude");
        }
        return dest.getConverter().convertToReferenceUnit(
                origin.getConverter().convertFromReferenceUnit(scalar)
        );
    }
    
    public boolean isOfMagnitude(Unit u){
        return Arrays.asList(this.units).contains(u);
    }
}

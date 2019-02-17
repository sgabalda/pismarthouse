/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author gabalca
 */
public final class Measure implements Comparable<Measure> {
    private final Magnitude magnitude;
    private final Scalar scalar;
    private final Unit unit;

    public Measure(Magnitude magnitude, Scalar scalar, Unit unit) {
        if(magnitude==null){
            throw new IllegalArgumentException(
                    "Can not create Measure: Magnitude null");
        }
        if(scalar==null){
            throw new IllegalArgumentException(
                    "Can not create Measure: scalar null");
        }
        if(unit==null){
            throw new IllegalArgumentException(
                    "Can not create Measure: unit null");
        }
        if(!magnitude.isOfMagnitude(unit)){
            throw new IllegalArgumentException("Can not create Measure: Unit "+unit+" is not of "+magnitude);
        }
        this.magnitude = magnitude;
        this.scalar = scalar;
        this.unit = unit;
    }
    
    public Magnitude getMagnitude(){
        return magnitude;
    }
    public Scalar getScalar(){
        return scalar;
    }
    public Unit getUnits(){
        return unit;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.magnitude);
        hash = 83 * hash + Objects.hashCode(this.scalar);
        hash = 83 * hash + Objects.hashCode(this.unit);
        return hash;
    }


    
    public Measure convertUnits(Unit unit){
        return new Measure(this.magnitude,this.magnitude.convert(this.scalar, this.unit, unit),unit);
        
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
        final Measure other = (Measure) obj;
        if (this.unit != other.unit) {
            if(this.magnitude.isConvertible(this.unit, other.unit)){
                return this.magnitude.convert(this.scalar, this.unit, other.unit).equals(other.scalar);
            }else{
                return false;
            }
        }
        return this.scalar.equals(other.scalar);
    }

    @Override
    public String toString() {
        return "Measure{" + "magnitude=" + magnitude + ", scalar=" + scalar + ", unit=" + unit + '}';
    }

    @Override
    public int compareTo(Measure arg0) {
        if(arg0==null) throw new NullPointerException();
        if(this.equals(arg0)) return 0;
        if(this.scalar.getValue() instanceof Number 
                && arg0.scalar.getValue() instanceof Number){
            if(this.magnitude.isConvertible(this.unit, arg0.unit)){
                Measure normalized=arg0.convertUnits(this.unit);
                return new BigDecimal(
                        ((Number)this.scalar.getValue()).doubleValue()
                ).compareTo(new BigDecimal(
                        ((Number)normalized.scalar.getValue()).doubleValue())
                );
            }else{
               throw new RuntimeException("Can not compare two mesures taht "
                       + "have no comparable units: "+this+ " vs " + arg0);
            }
        }else{
            throw new UnsupportedOperationException("Comparison pf Measures of "
                    + "type not number not yet supported");
        }
    }
    
    
}

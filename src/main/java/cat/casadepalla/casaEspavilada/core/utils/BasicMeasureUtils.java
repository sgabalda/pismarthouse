/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.utils;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.MeasureUtils;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.math.BigDecimal;

/**
 *
 * @author gabalca
 */
public class BasicMeasureUtils implements MeasureUtils{

    @Override
    public Measure addMeasures(Measure m,Measure... measures) {
        if(measures==null || measures.length==0) return m;
        
        Magnitude magn=m.getMagnitude();
        Unit unit=m.getUnits();
        
        BigDecimal number=new BigDecimal(((Number)m.getScalar().getValue()).doubleValue());
        
        for(Measure m1:measures){
            Scalar<Number> m1Scalar=magn.convert(m1.getScalar(), m1.getUnits(), unit);
            number=number.add(new BigDecimal(m1Scalar.getValue().doubleValue()));
        }
        return new Measure(magn, new Scalar<>(number.doubleValue()), unit);
    }

    @Override
    public Measure substractMeasures(Measure m, Measure... measures) {
        if(measures==null || measures.length==0) return m;
        
        Magnitude magn=m.getMagnitude();
        Unit unit=m.getUnits();
        
        BigDecimal number=new BigDecimal(((Number)m.getScalar().getValue()).doubleValue());
        
        for(Measure m1:measures){
            Scalar<Number> m1Scalar=magn.convert(m1.getScalar(), m1.getUnits(), unit);
            number=number.subtract(new BigDecimal(m1Scalar.getValue().doubleValue()));
        }
        return new Measure(magn, new Scalar<>(number.doubleValue()), unit);
    }
    
}

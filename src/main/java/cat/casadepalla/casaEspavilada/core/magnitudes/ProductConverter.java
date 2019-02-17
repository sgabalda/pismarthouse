/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

import java.math.BigDecimal;

/**
 *
 * @author gabalca
 */
public class ProductConverter implements UnitConverter<Number>{
    
    private double ratio;

    public ProductConverter(double ratio) {
        if(ratio==0)throw new IllegalArgumentException("ProductConverter: ratio can not be 0");
        this.ratio = ratio;
    }

    @Override
    public Scalar<Number> convertToReferenceUnit(Scalar<Number> s) {
        if(s==null)throw new IllegalArgumentException("Can not convert a null scalar");
        BigDecimal result=BigDecimal.valueOf(s.getValue().doubleValue()).multiply(BigDecimal.valueOf(ratio));
        return new Scalar<>(result.doubleValue());
    }

    @Override
    public Scalar<Number> convertFromReferenceUnit(Scalar<Number> s) {
        if(s==null)throw new IllegalArgumentException("Can not convert a null scalar");
        BigDecimal result=BigDecimal.valueOf(s.getValue().doubleValue()).divide(BigDecimal.valueOf(ratio));
        return new Scalar<>(result.doubleValue());
    }
    
}

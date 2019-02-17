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
public interface UnitConverter<T> {
    public Scalar<T> convertToReferenceUnit(Scalar<T> s);
    public Scalar<T> convertFromReferenceUnit(Scalar<T> s);
}


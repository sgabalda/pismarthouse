/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author gabalca
 */
public final class Scalar<T> {
    private final T value;

    public Scalar(T value) {
        if(value==null)throw new IllegalArgumentException("Scalar can not contain a null value");
        this.value = value;
    }
    
    public T getValue(){
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
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
        final Scalar<?> other = (Scalar<?>) obj;
        Object thisValue=this.value;
        Object otherValue=other.value;
        if(thisValue instanceof Number && otherValue instanceof Number){
            //System.out.println("are both numbers "+thisValue+" - "+otherValue);
            return new BigDecimal(((Number)thisValue).doubleValue())
                    .compareTo(new BigDecimal(((Number)otherValue).doubleValue()))==0;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Scalar{" + "value=" + value + '}';
    }
    
    
    
}

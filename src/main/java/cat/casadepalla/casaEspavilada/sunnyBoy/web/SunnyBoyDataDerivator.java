/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.Measure;
import java.util.Map;

/**
 *
 * @author gabalca
 */
public interface SunnyBoyDataDerivator {
    
    public Map<String,Measure> derivate(Map<String,Measure> original)
            throws SunnyBoyDerivatorAdapterException;
    
}

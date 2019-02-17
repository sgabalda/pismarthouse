/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.Measurements;
import java.util.Map;

/**
 *
 * @author gabalca
 */
public interface SunnyBoyDataAdapter {

    public Measurements getMeasurements(Map<String,String> data) throws SunnyBoyAdapterException;
    
}

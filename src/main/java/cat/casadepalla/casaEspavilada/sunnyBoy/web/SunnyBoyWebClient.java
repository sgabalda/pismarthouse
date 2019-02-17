/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import java.util.Map;

/**
 *
 * @author gabalca
 */
public interface SunnyBoyWebClient {
    
    public Map<String,String> getValues(String ... codes) throws SunnyBoyWebClientException;
    
}

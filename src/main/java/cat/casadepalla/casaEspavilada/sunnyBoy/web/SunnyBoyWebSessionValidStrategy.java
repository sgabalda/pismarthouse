/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

/**
 *
 * @author gabalca
 */
public interface SunnyBoyWebSessionValidStrategy {
    
    public boolean isTokenValid();
    public void tokenUpdated();
    
}

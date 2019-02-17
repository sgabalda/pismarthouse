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
public interface SunnyBoyWebSessionManager {
    /**
     * returns the current session token
     * @return 
     * @throws cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyWebClientException if could not get the token
     */
    public String getSessionToken() throws SunnyBoyWebClientException ;
    /**
     * forces the refresh of the session token
     * @throws cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyWebClientException if could not refresh the session
     */
    public void refreshToken() throws SunnyBoyWebClientException ;
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;

/**
 *
 * @author gabalca
 */
public interface SunnyBoyConfigResolver {
    
    /**
     * Returns an array with all the numeric codes that must be read from the Sunny Boy
     * @return 
     */
    public String [] getSunnyBoyDataCodesToRead();
    /**
     * returns the sunny boy Login URL
     * @return 
     */
    public String getLoginURL();
    /**
     * returns the sunny boy data URL
     * @return 
     */
    public String getDataUrl();
    /**
     * Returns the sunny boy username, usually "usr", as it is the user group
     * @return 
     */
    public String getUsername();
    /**
     * returns the Sunny Boy password. Is a secret, as it gives acces tot he sunny boy console
     * @return 
     */
    public String getPassword();
    /**
     * gets the sunny boy session param. It is the query string param where the session id is sent to each 
     * request after login. Usually "sid"
     * @return 
     */
    public String getSessionParam();
    /**
     * returns the internal code for a sunny boy code
     * @param sunnyBoyNumericCode
     * @return 
     */
    public String getDataCode(String sunnyBoyNumericCode);
    /**
     * returns the units for an internal code
     * @param code
     * @return 
     */
    public Unit getDataUnits(String code);
    /**
     * returns the Magnitude for an internal code
     * @param code
     * @return 
     */
    public Magnitude getDataMagnitude(String code);
    /**
     * returns the type for an internal code and its value,
     * the types may be double, long and string
     * @param code
     * @param value
     * @return 
     */
    public Scalar getDataType(String code, String value);
    
}

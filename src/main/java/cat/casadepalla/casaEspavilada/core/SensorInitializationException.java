/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

/**
 *
 * @author gabalca
 */
public class SensorInitializationException extends SensorException {

    public SensorInitializationException() {
    }

    public SensorInitializationException(String message) {
        super(message);
    }

    public SensorInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SensorInitializationException(Throwable cause) {
        super(cause);
    }
    
}

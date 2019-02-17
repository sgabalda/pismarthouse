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
public class SunnyBoyWebClientException extends Exception {

    private String responseBody;

    public String getResponseBody() {
        return responseBody;
    }

    public SunnyBoyWebClientException(String message,String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }

    public SunnyBoyWebClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SunnyBoyWebClientException(Throwable cause) {
        super(cause);
    }


    
}

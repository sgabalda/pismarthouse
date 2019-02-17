/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app.diProviders;

import java.time.Clock;
import javax.enterprise.inject.Produces;

/**
 *
 * @author gabalca
 */
public class ClockProducer {
    
    @Produces
    public static Clock clockProducer(){
        return Clock.systemDefaultZone();
    }
    
}

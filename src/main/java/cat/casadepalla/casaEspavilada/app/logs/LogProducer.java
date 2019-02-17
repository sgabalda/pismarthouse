/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app.logs;

import cat.casadepalla.casaEspavilada.app.diProviders.ConfigValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author gabalca
 */
@Singleton
public class LogProducer {
    
    @Inject
    public LogProducer(@ConfigValue(code="logging.configfile") String filename){
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream(file));
            Logger.getLogger("").info("LOADED LOGGING CONFIG IN "+filename);
        } catch (IOException exception) {
            Logger.getLogger("").log(Level.SEVERE, "Error in loading log configuration",exception);
        }
    }
    
    @Produces
    public Logger createLogger(InjectionPoint injectionPoint) {

      return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());

   }
    
}

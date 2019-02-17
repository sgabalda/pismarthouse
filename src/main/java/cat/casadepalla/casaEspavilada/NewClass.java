/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import cat.casadepalla.casaEspavilada.core.SensorMeasurementException;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.GenericSunnyBoyWebInterfaceSensor;
import java.util.Map;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 *
 * @author gabalca
 */
public class NewClass {
    
    public static void main(String args[]) throws SensorMeasurementException{

        try (WeldContainer container = new Weld().initialize()) {

            GenericSunnyBoyWebInterfaceSensor sensor=
                    container.select(GenericSunnyBoyWebInterfaceSensor.class).get();
            
            Processor p=(Measurements measures) -> {
                System.out.println("Measurement taken:");
                Map<String,Measure> allMeasures=measures.getAllMeasures();
                allMeasures.keySet().forEach((_item) -> {
                    System.out.println("Measure "+_item+" => "+allMeasures.get(_item));
                });
            };
            
            sensor.measure(p);

        }
    }
    
}

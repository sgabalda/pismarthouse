/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import cat.casadepalla.casaEspavilada.core.SensorMeasurementException;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.GenericSunnyBoyWebInterfaceSensor;
import java.util.Map;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author gabalca
 */
public class PowerMeasurementIntegrationTest {
    
    public PowerMeasurementIntegrationTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreatingASunnyBoyPowerSensor() throws SensorMeasurementException{
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

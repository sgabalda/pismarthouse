/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class CombinedMeasurementsTest {
    
    public CombinedMeasurementsTest() {
    }
    
    private CombinedMeasurements measurements;
    private Map<String,Measure> measures;
    private Measure m1;
    private Measure m2;
    private Measure m3;
    
    private long beforeCreating;
    private long afterCreating;
    
    @BeforeEach
    public void setUp() {
        m1=new Measure(Magnitude.POWER,new Scalar<>(11.867),Unit.W);
        m2=new Measure(Magnitude.POWER,new Scalar<>(1.27),Unit.kW);
        m3=new Measure(Magnitude.ENERGY,new Scalar<>(2.2),Unit.kWh);
        
        measures=new HashMap<>();
        measures.put("ProducedPower", m1);
        measures.put("InstalledPower", m2);
        measures.put("StoredEnergy", m3);
        
        beforeCreating=System.currentTimeMillis();
        measurements=new CombinedMeasurements(measures);
        afterCreating=System.currentTimeMillis();
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getMeasurementsNames method, of class CombinedMeasurements.
     */
    @Test
    public void testGetMeasurementsNamesContainsAllNames() {
        assertTrue(measurements
                .getMeasurementsNames()
                .containsAll(Arrays.asList(
                    "ProducedPower",
                    "InstalledPower",
                    "StoredEnergy"
                )));
    }
    
    @Test
    public void testGetMeasurementsNamesContainsNoMoreNames() {
        assertEquals(3,measurements
                .getMeasurementsNames().size());
    }
    /**
     * Test of getMeasurementTime method, of class CombinedMeasurements.
     */
    @Test
    public void testGetMeasurementTime() {
        assertTrue(afterCreating>=measurements.getMeasurementTime());
        assertTrue(beforeCreating<=measurements.getMeasurementTime());
    }

    /**
     * Test of getMeasure method, of class CombinedMeasurements.
     */
    @Test
    public void testGetMeasure() {
        assertEquals(
                m1, 
                measurements.getMeasure("ProducedPower")
        );
    }
    
    @Test
    public void testGetMeasureFailsIfNotCorrectName() {
        Executable ex=()->{
            measurements.getMeasure("NotValid");
        };
        assertThrows(IllegalArgumentException.class, ex,"getMeasure Must fail if the name is not valid");    
        
    }

    @Test
    public void testMeasuresCanNotBeModified() {
        measures.remove("ProducedPower");
        assertNotNull(measurements.getMeasure("ProducedPower"));
    }
    
    /**
     * Test of getAllMeasures method, of class CombinedMeasurements.
     */
    @Test
    public void testGetAllMeasuresContainsNamesAndMeasures() {
        Map<String,Measure> measuresAll=measurements.getAllMeasures();
        
        assertEquals(m1,measuresAll.get("ProducedPower"));
        assertEquals(m2,measuresAll.get("InstalledPower"));
        assertEquals(m3,measuresAll.get("StoredEnergy"));
        
        assertEquals(3, measuresAll.size());
    }
    
    @Test
    public void testGetAllMeasuresDoesNotModifyMeasures() {
         Map<String,Measure> measuresAll=measurements.getAllMeasures();
         measuresAll.put("oeoe", m1);
         
         assertEquals(3, measurements.getAllMeasures().size());
    }
    
}

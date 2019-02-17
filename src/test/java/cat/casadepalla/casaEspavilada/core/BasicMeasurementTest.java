/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class BasicMeasurementTest {
    
    public BasicMeasurementTest() {
    }
    
    private BasicMeasurement measurement;
    private long beforeCreating;
    private long afterCreating;
    
    @BeforeEach
    public void setUp() {
        beforeCreating=System.currentTimeMillis();
        measurement=new BasicMeasurement(
                new Measure(Magnitude.POWER, new Scalar<>(1.5), Unit.kW),
                "ProducedPower"
        );
        afterCreating=System.currentTimeMillis();
    }
    
    @AfterEach
    public void tearDown() {
    }

    
    
    /**
     * Test of getMeasurementTime method, of class BasicMeasurement.
     */
    @Test
    public void testGetMeasurementTime() {
        assertTrue(afterCreating>=measurement.getMeasurementTime());
        assertTrue(beforeCreating<=measurement.getMeasurementTime());
    }

    /**
     * Test of getMeasure method, of class BasicMeasurement.
     */
    @Test
    public void testGetMeasure() {
        assertEquals(
                new Measure(Magnitude.POWER, new Scalar<>(1.5), Unit.kW), 
                measurement.getMeasure("ProducedPower")
        );
    }
    
    @Test
    public void testGetMeasureCanNotReturnIfWrongName() {
        Executable ex=()->{
            measurement.getMeasure("ProducedPower2");
        };
        
       assertThrows(IllegalArgumentException.class, ex,"Must not allow ask for a non existent name measure");    
        
    }
     @Test
    public void testDoesNotAllowNullName() {
        Executable ex=()->{
            new BasicMeasurement(new Measure(Magnitude.POWER, new Scalar<>(1.5), Unit.kW),null);
        };
        
       assertThrows(IllegalArgumentException.class, ex,"Must not allow null name");    
        
    }
    @Test
    public void testDoesNotAllowNullMeasure() {
        Executable ex=()->{
            new BasicMeasurement(null,"Power");
        };
        
       assertThrows(IllegalArgumentException.class, ex,"Must not allow null measure");    
        
    }

    /**
     * Test of getMeasurementsNames method, of class BasicMeasurement.
     */
    @Test
    public void testGetMeasurementsNamesReturnsName() {
        assertTrue(measurement.getMeasurementsNames().contains("ProducedPower"));
    }
    
    @Test
    public void testGetMeasurementsNamesReturnOnlyOneElement() {
        assertEquals(1,measurement.getMeasurementsNames().size());
    }

    /**
     * Test of getAllMeasures method, of class BasicMeasurement.
     */
    @Test
    public void testGetAllMeasuresContainsTheMeasure() {
        assertTrue(
                measurement
                .getAllMeasures()
                        .get("ProducedPower")
                        .equals(
                                new Measure(Magnitude.POWER, 
                                        new Scalar<>(1.5), 
                                        Unit.kW)
                        )
        );
    }
    @Test
    public void testGetAllMeasuresContainsOnlyOneMeasure() {
        assertEquals(1,
                measurement
                .getAllMeasures()
                        .size()
        );
    }

    /**
     * Test of toString method, of class BasicMeasurement.
     */
    @Test
    public void testToString() {
        assertTrue(measurement.toString().contains("POWER"));
        assertTrue(measurement.toString().contains("1.5"));
        assertTrue(measurement.toString().contains("kW"));
    }
    
    
    
}

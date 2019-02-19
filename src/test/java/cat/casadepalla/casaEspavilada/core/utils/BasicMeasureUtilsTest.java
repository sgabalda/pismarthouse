/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.utils;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 *
 * @author gabalca
 */
public class BasicMeasureUtilsTest {
    
    public BasicMeasureUtilsTest() {
    }
    BasicMeasureUtils sut;
    
    @BeforeEach
    public void setUp() {
        sut=new BasicMeasureUtils();
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of addMeasures method, of class BasicMeasureUtils.
     */
    @Test
    public void testAddMeasures() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh); 
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(0.007), Unit.kWh);
        
        Measure result=new Measure(Magnitude.ENERGY, new Scalar<>(19.5), Unit.Wh);
        assertEquals(result, sut.addMeasures(mg,th,m));
    }
    
    @Test
    public void testSubstractMeasures() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh); 
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(0.007), Unit.kWh);
        
        Measure result=new Measure(Magnitude.ENERGY, new Scalar<>(-15.5), Unit.Wh);
        assertEquals(result, sut.substractMeasures(mg,th,m));
    }
    
}

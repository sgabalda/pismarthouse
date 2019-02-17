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
public class MeasureTest {
    
    private Measure measure;
    private Measure measure2;
    private Measure measure3;
    
    public MeasureTest() {
    }
    
    @BeforeEach
    public void setUp() {
        measure=new Measure(Magnitude.POWER,new Scalar<>(11.867),Unit.W);
        measure2=new Measure(Magnitude.POWER,new Scalar<>(11.867),Unit.W);
        measure3=new Measure(Magnitude.POWER,new Scalar<>(0.011867),Unit.kW);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getMagnitude method, of class Measure.
     */
    @Test
    public void testGetMagnitude() {
        assertEquals( Magnitude.POWER, measure.getMagnitude());
    }

    /**
     * Test of getScalar method, of class Measure.
     */
    @Test
    public void testGetScalar() {
        assertEquals(11.867, measure.getScalar().getValue());
    }

    /**
     * Test of getUnits method, of class Measure.
     */
    @Test
    public void testGetUnits() {
        assertEquals(Unit.W, measure.getUnits());
    }
    
    @Test
    public void testEquals(){
        assertEquals(measure, measure2);
    }
    
    @Test
    public void testEqualsIfUnitsConvertible(){
        assertEquals(measure, measure3);
    }
    
    @Test
    public void testNotEqualsIfUnitsNotConvertible(){
        assertNotEquals(measure, new Measure(Magnitude.ENERGY,new Scalar<>(0.021867),Unit.kWh));
    }
    
    @Test
    public void testNotEqualsIfUnitsConvertibleButDifferent(){
        assertNotEquals(measure, new Measure(Magnitude.POWER,new Scalar<>(0.021867),Unit.kW));
    }
    @Test
    public void testCanNotBeCreatedWithNullMagnitude(){

        
        Executable ex=()->{
             Measure measure2=new Measure(null,new Scalar<>(11),Unit.W);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Must not allow "
                + "creation if null amgnitude");    
        
    }
    @Test
    public void testCanNotBeCreatedWithNullScalar(){

        
        Executable ex=()->{
             Measure measure2=new Measure(Magnitude.POWER,null,Unit.W);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Must not allow "
                + "creation if null scalar");    
        
    }
    @Test
    public void testCanNotBeCreatedWithNullUnit(){

        
        Executable ex=()->{
             Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Must not allow "
                + "creation if null unit");    
        
    }
    
    @Test
    public void testEqualsIfScalarsOfDifferentTypeButSameAmount(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11.0),Unit.W);
        assertEquals(measure, measure2);
    }
    @Test
    public void testEqualsIfScalarsOfDifferentTypeAndUnitButSameAmount(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(0.011),Unit.kW);
        assertEquals(measure, measure2);
    }
    
    @Test
    public void testCompareToReturns0IfMeasuresSimilar(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        assertEquals(0, measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturnsNegativeIfMeasuresSimilarButLower(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(10),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        assertTrue(0>measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturnsPositiveIfMeasuresSimilarButGreater(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(12),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        assertTrue(0<measure.compareTo(measure2));
    }
    
    @Test
    public void testCompareToReturnsPositiveIfMeasureGreaterButDifferentUnits(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(0.012),Unit.kW);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        assertTrue(0<measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturnsNegativeIfMeasureGreaterButDifferentUnits(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(0.01),Unit.kW);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        assertTrue(0>measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturnsPositiveIfMeasureGreaterButDecreasingRatioUnits(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(12),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(0.011),Unit.kW);
        assertTrue(0<measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturnsNegativeIfMeasureGreaterButDecreasimgRatioUnits(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(10),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(0.011),Unit.kW);
        assertTrue(0>measure.compareTo(measure2));
    }
    @Test
    public void testCompareTothrowsRTEIfUnitsNotComparable(){
        Measure measure=new Measure(Magnitude.FREQUENCY,new Scalar<>(0.012),Unit.Hz);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        
        Executable ex=()->{
            measure.compareTo(measure2);
        };
        
        assertThrows(RuntimeException.class, ex,"Must not allow compare if units are not comparable");    
        
    }
    @Test
    public void testCompareToReturns0IfScalarsOfDifferentTypeButSameAmount(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(11.0),Unit.W);
        assertEquals(0, measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturns0IfDifferentUnitButSameAmount(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(0.011),Unit.kW);
        assertEquals(0, measure.compareTo(measure2));
    }
    @Test
    public void testCompareToReturns0IfDifferentUnitAndScalarTypeButSameAmount(){
        Measure measure=new Measure(Magnitude.POWER,new Scalar<>(11),Unit.W);
        Measure measure2=new Measure(Magnitude.POWER,new Scalar<>(0.011),Unit.kW);
        assertEquals(0, measure.compareTo(measure2));
    }
    
    @Test
    public void testConvertWToKw(){
        Measure converted=measure.convertUnits(Unit.kW);
        assertEquals(0.011867, converted.getScalar().getValue());
        assertEquals(Unit.kW, converted.getUnits());
    }
    
    @Test
    public void testConvertKWToW(){
        Measure converted=measure3.convertUnits(Unit.W);
        assertEquals(11.867, converted.getScalar().getValue());
        assertEquals(Unit.W, converted.getUnits());
    }
    
    @Test
    public void testCanNotConvertIfDifferentMangitudeUnits(){
        Executable ex=()->{
            Measure converted=measure3.convertUnits(Unit.Wh);
        };
        
       assertThrows(IllegalArgumentException.class, ex,"Must not convert if units not of the same magnitude");    
        
    }
    
    @Test
    public void testCanNotCreateMeasureOfUnitNotOfMagnitude(){
        Executable ex=()->{
            Measure converted=new Measure(Magnitude.ENERGY, new Scalar<>(12.0), Unit.W);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Must not allow create Measure if units not of the magnitude");    
        
        
    }
}

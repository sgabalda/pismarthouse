/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class MagnitudeTest {
    
    public MagnitudeTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of isConvertible method, of class Magnitude.
     */
    @Test
    public void testIsKWToWConvertible() {
        assertTrue(Magnitude.POWER.isConvertible(Unit.kW, Unit.W));
    }
    /**
     * Test of isConvertible method, of class Magnitude.
     */
    @Test
    public void testIsKWTokKWhNotConvertible() {
        assertFalse(Magnitude.POWER.isConvertible(Unit.kW, Unit.kWh));
    }
    
    /**
     * Test of isConvertible method, of class Magnitude.
     */
    @Test
    public void testIsWtoWConvertible() {
        assertTrue(Magnitude.POWER.isConvertible(Unit.W, Unit.W));
    }
    
    @Test
    public void testConvertWToWStaysTheSame(){
        Scalar<Double> result=Magnitude.POWER.convert(new Scalar<>(123.1234),Unit.W,Unit.W);
        assertEquals(123.1234,(double) result.getValue());
    }
    
    @Test
    public void testConvertkWTokWStaysTheSame(){
        Scalar<Double> result=Magnitude.POWER.convert(new Scalar<>(123.1234),Unit.kW,Unit.kW);
        assertEquals(123.1234,(double) result.getValue());
    }
    
    @Test
    public void testConvertWTokWis10TimesSmaller(){
        Scalar<Double> result=Magnitude.POWER.convert(new Scalar<>(123.1234),Unit.W,Unit.kW);
        assertEquals(0.1231234,(double) result.getValue());
    }
    
        @Test
    public void testConvertkWToWis10TimesGreater(){
        Scalar<Double> result=Magnitude.POWER.convert(new Scalar<>(123.1234),Unit.kW,Unit.W);
        assertEquals(123123.4,(double) result.getValue());
    }
    @Test
    public void testCanNotConvertIfNotTheSameMagnitude(){
        Executable ex=()->{
            Scalar<Double> result=Magnitude.POWER.convert(new Scalar<>(123.1234),Unit.kWh,Unit.W);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Must not convert if units not of the same magnitude");    
        
    }
    
    @Test
    public void testGetUnits(){
        for(Magnitude m:Magnitude.values()){
            assertNotNull(m.getUnits());
            assertNotEquals(0, m.getUnits().length);
        }
    }
    @Test
    public void testUnitIsOfMagnitude(){
        for(Magnitude m:Magnitude.values()){
            for(Magnitude m2:Magnitude.values()){
                for(Unit u:m2.getUnits()){
                    if(m==m2){
                        assertTrue(m.isOfMagnitude(u),"Unit "+u+" must be of magnitude "+m);
                    }else{
                        assertFalse(m.isOfMagnitude(u),"Unit "+u+" must NOT be of magnitude "+m);
                    }
                }
            }
        }
            
    }
}

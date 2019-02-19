/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.switches;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 *
 * @author gabalca
 */
public class HisteresisMagnitudeBasedSwitchDecoratorTest {
    
    public HisteresisMagnitudeBasedSwitchDecoratorTest() {
    }
    
    private Switch decSwitch;
    private Measure treshold;
    private Measure margin;
    private HisteresisMagnitudeBasedSwitchDecorator sut;
    
    @BeforeEach
    public void setUp() {
        decSwitch=mock(Switch.class);
        treshold=mock(Measure.class);
        margin=mock(Measure.class);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch,treshold,margin);
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testCanNotBeCreatedWithNullSwitch() {
        Executable ex=()->{
            sut=new HisteresisMagnitudeBasedSwitchDecorator(null, treshold,margin);
        };
        assertThrows(IllegalArgumentException.class, ex,"HisteresisMagnitudeBasedSwitchDecorator"
                + " can not be set a null switch");
    }
    @Test
    public void testCanNotBeCreatedWithNullTreshold() {
        Executable ex=()->{
            sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, null,margin);
        };
        assertThrows(IllegalArgumentException.class, ex,"HisteresisMagnitudeBasedSwitchDecorator"
                + " can not be set a null treshold");
    }
    @Test
    public void testCanNotBeCreatedWithNullMargin() {
        Executable ex=()->{
            sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, treshold,null);
        };
        assertThrows(IllegalArgumentException.class, ex,"HisteresisMagnitudeBasedSwitchDecorator"
                + " can not be set a null margin");
    }
    
    @Test
    public void testCanNotBeCreatedWithNonComparableTresholdAndMargin() {
        Executable ex=()->{
            Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(10), Unit.Wh);
            Measure th=new Measure(Magnitude.POWER, new Scalar<>(10), Unit.kW);
            sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        };
        RuntimeException e=assertThrows(IllegalArgumentException.class, ex,"HisteresisMagnitudeBasedSwitchDecorator"
                + " can not be set a non comparable margin-treshold pair");
        assertTrue(e.getMessage().contains("HisteresisMagnitudeBasedSwitchDecorator"));
    }
    @Test
    public void testCanBeCreatedWithNonComparableTresholdAndMargin() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(10), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10), Unit.kWh);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
    }

    /**
     * Test of wouldTurnOnWithMeasure method, of class HisteresisMagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testWouldTurnOnIfAboveTresholdAndMarginAndWasOff() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(false);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(14), Unit.Wh);
        
        assertTrue(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOnIfAboveTresholdAndMarginAndWasOn() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(true);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(14), Unit.Wh);
        
        assertTrue(sut.wouldTurnOnWithMeasure(m));
    }
    @Test
    public void testWouldTurnOffIfBetweenTresholdAndMarginAndWasOff() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(false);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(11), Unit.Wh);
        
        assertFalse(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOnIfBetweenTresholdAndMarginAndWasOn() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(true);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(11), Unit.Wh);
        
        assertTrue(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOffIfBetweenTresholdAndMinusMarginAndWasOff() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(false);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(9), Unit.Wh);
        
        assertFalse(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOnIfBetweenTresholdAndMinusMarginAndWasOn() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(true);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(9), Unit.Wh);
        
        assertTrue(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOffIfBelowTresholdAndMinusMarginAndWasOff() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(false);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(7), Unit.Wh);
        
        assertFalse(sut.wouldTurnOnWithMeasure(m));
    }
    
    @Test
    public void testWouldTurnOffIfBelowTresholdAndMinusAndWasOn() {
        Measure mg=new Measure(Magnitude.ENERGY, new Scalar<>(2), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(10.5), Unit.Wh);
        when(decSwitch.isOn()).thenReturn(true);
        sut=new HisteresisMagnitudeBasedSwitchDecorator(decSwitch, th,mg);
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(7), Unit.Wh);
        
        assertFalse(sut.wouldTurnOnWithMeasure(m));
    }
           
    
    
    
}

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
import javax.management.RuntimeErrorException;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 *
 * @author gabalca
 */
public class MagnitudeBasedSwitchDecoratorTest {
    
    public MagnitudeBasedSwitchDecoratorTest() {
    }
    private Switch decSwitch;
    private Measure treshold;
    private MagnitudeBasedSwitchDecorator sut;
    
    @BeforeEach
    public void setUp() {
        decSwitch=mock(Switch.class);
        treshold=mock(Measure.class);
        sut=new MagnitudeBasedSwitchDecorator(decSwitch,treshold);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getTreshold method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testGetTreshold() {
        assertEquals(treshold,sut.getTreshold());
    }

    /**
     * Test of setTreshold method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testSetTreshold() {
        Measure treshMeasure=mock(Measure.class);
        assertTrue(treshMeasure!=treshold);
        sut.setTreshold(treshMeasure);
        assertEquals(treshMeasure, sut.getTreshold());
    }
    @Test
    public void testSetTresholdFailsIfTresholdNull() {
        Executable ex=()->{
            sut.setTreshold(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"MagnitudeBasedSwitchDecorator"
                + " can not be set a null treshold");
    }
    
    @Test
    public void testSetTresholdFailsIfTresholdNotComparable() {
        Executable ex=()->{
             when(treshold.compareTo(ArgumentMatchers.any())).thenThrow(RuntimeErrorException.class);
            sut.setTreshold(treshold);
        };
        assertThrows(IllegalArgumentException.class, ex,"MagnitudeBasedSwitchDecorator"
                + " can not be set a non comparable treshold");
    }
    
    @Test
    public void testConstructorFailsIfTresholdNull() {
        Executable ex=()->{
            sut=new MagnitudeBasedSwitchDecorator(decSwitch, null);
        };
        assertThrows(IllegalArgumentException.class, ex,"MagnitudeBasedSwitchDecorator"
                + " can not be set a null treshold");
    }
    
    @Test
    public void testConstructorFailsIfSwitchNull() {
        Executable ex=()->{
            sut=new MagnitudeBasedSwitchDecorator(null, treshold);
        };
        assertThrows(IllegalArgumentException.class, ex,"MagnitudeBasedSwitchDecorator"
                + " can not be set a null switch");
    }
    
    @Test
    public void testConstructorFailsIfTresholdNotComparable() {
        Executable ex=()->{
             when(treshold.compareTo(ArgumentMatchers.any())).thenThrow(RuntimeErrorException.class);
            sut=new MagnitudeBasedSwitchDecorator(decSwitch, treshold);
        };
        assertThrows(IllegalArgumentException.class, ex,"MagnitudeBasedSwitchDecorator"
                + " can not be set a non comparable treshold");
    }

    /**
     * Test of turnOn method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testTurnOnDelegatesToDecoratedSwitch() {
        sut.turnOn();
        verify(decSwitch,times(1)).turnOn();
    }

    /**
     * Test of turnOff method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testTurnOffDelegatesToDecoratedSwitch() {
        sut.turnOff();
        verify(decSwitch,times(1)).turnOff();
    }

    /**
     * Test of isOn method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testIsOnDelegatesToDecoratedSwitchAndReturnsTrue() {
        when(decSwitch.isOn()).thenReturn(Boolean.TRUE);
        assertTrue(sut.isOn());
        verify(decSwitch,times(1)).isOn();
    }
    @Test
    public void testIsOnDelegatesToDecoratedSwitchAndReturnsFalse() {
        when(decSwitch.isOn()).thenReturn(Boolean.FALSE);
        assertFalse(sut.isOn());
        verify(decSwitch,times(1)).isOn();
    }

    /**
     * Test of turnWithMeasure method, of class MagnitudeBasedSwitchDecorator.
     */
    @Test
    public void testTurnWithMeasureCallsOffIfBelowTreshold() {
        when(treshold.compareTo(ArgumentMatchers.any())).thenReturn(1);
        sut.turnWithMeasure(treshold);
        verify(decSwitch,times(1)).turnOff();
    }
    @Test
    public void testTurnWithMeasureCallsOnIfAboveTreshold() {
        when(treshold.compareTo(ArgumentMatchers.any())).thenReturn(-1);
        sut.turnWithMeasure(treshold);
        verify(decSwitch,times(1)).turnOn();
    }
    @Test
    public void testTurnWithMeasureCallsOffIfBelowTresholdConcrete() {
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(11.0), Unit.Wh);
        sut.setTreshold(th);
        sut.turnWithMeasure(m);
        verify(decSwitch,times(1)).turnOff();
    }
    @Test
    public void testTurnWithMeasureCallsOnIfAboveTresholdConcrete() {
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(9.0), Unit.Wh);
        sut.setTreshold(th);
        sut.turnWithMeasure(m);
        verify(decSwitch,times(1)).turnOn();
    }
    @Test
    public void testWouldTurnWithMeasureCallsOffIfBelowTreshold() {
        when(treshold.compareTo(ArgumentMatchers.any())).thenReturn(1);
        assertFalse(sut.wouldTurnOnWithMeasure(treshold));
    }
    @Test
    public void testWouldTurnWithMeasureCallsOnIfAboveTreshold() {
        when(treshold.compareTo(ArgumentMatchers.any())).thenReturn(-1);
        assertTrue(sut.wouldTurnOnWithMeasure(treshold));
    }
    @Test
    public void testWouldTurnWithMeasureCallsOffIfBelowTresholdConcrete() {
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(11.0), Unit.Wh);
        sut.setTreshold(th);
        assertFalse(sut.wouldTurnOnWithMeasure(m));
    }
    @Test
    public void testWouldTurnWithMeasureCallsOnIfAboveTresholdConcrete() {
        Measure m=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Measure th=new Measure(Magnitude.ENERGY, new Scalar<>(9.0), Unit.Wh);
        sut.setTreshold(th);
        assertTrue(sut.wouldTurnOnWithMeasure(m));
    }
    
    
}

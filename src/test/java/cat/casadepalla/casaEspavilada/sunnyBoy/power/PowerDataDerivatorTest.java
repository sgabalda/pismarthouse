/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.power;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.FixedTimeoutSunnyBoyWebSessionValidStrategy;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyConfigResolver;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyDataDerivator;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyDerivatorAdapterException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author gabalca
 */
public class PowerDataDerivatorTest {
    
    public PowerDataDerivatorTest() {
    }
    
    private PowerDataDerivator sut;
    private PowerDataDerivatorConfig config;
    
    public static final double LOWER_LIMIT=50.0;
    public static final double UPPER_LIMIT=52.0;
    public static final String PRODUCED_POWER_CODE="ProducedPower";
    public static final String FREQUENCY_CODE="Frequency";
    public static final String NOT_USED_POWER_CODE="UnusedPower";
    public static final Unit LIMIT_UNIT=Unit.Hz;
    
    @BeforeEach
    public void setUp() {
        config=mock(PowerDataDerivatorConfig.class);
        sut=new PowerDataDerivator(config);
        sut.addLogger(mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of derivate method, of class PowerDataDerivator.
     */
    @Test
    public void testCanNotBeCreatedWithNullConfig() {
         Executable ex=()->{
            new PowerDataDerivator(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"PowerDataDerivator must throw "
                + "exception when created with null config");
    
    }
    
    
    
    @Test
    public void testDerivateFailsIfProducedPowerNotPresent() throws SunnyBoyDerivatorConfigException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        Map<String, Measure> original=new HashMap<>();
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(5012.0), Unit.cHz));
        Executable ex=()->{
            sut.derivate(original);
        };
        assertThrows(SunnyBoyDerivatorAdapterException.class, ex,"PowerDataDerivator must throw "
                + "exception if produced power measure is not present");
    }
    
    @Test
    public void testDerivateFailsIfFrequencyNotPresent() throws SunnyBoyDerivatorConfigException {
        Map<String, Measure> original=new HashMap<>();
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2123.0), Unit.W));
        Executable ex=()->{
            sut.derivate(original);
        };
        assertThrows(SunnyBoyDerivatorAdapterException.class, ex,"PowerDataDerivator must throw "
                + "exception if freq measure is not present");
    }
    
    @Test
    public void testDerivateFailsIfFrequencyCodeIsNotOfFrequencyMagnitude() throws SunnyBoyDerivatorConfigException {
        Map<String, Measure> original=new HashMap<>();
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2123.0), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2123.0), Unit.W));
        Executable ex=()->{
            sut.derivate(original);
        };
        assertThrows(SunnyBoyDerivatorAdapterException.class, ex,"PowerDataDerivator must throw "
                + "exception if freq measure is not present");
    }
    @Test
    public void testDerivateReturns0ifFrequencyIsOnLowLimit() 
            throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2123.0), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(5000.0), Unit.cHz));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, new Scalar<>(0), Unit.kW)
                , result.get(NOT_USED_POWER_CODE));
    }
    
    @Test
    public void testDerivateReturns0ifFreqUnderLowLimit() throws SunnyBoyDerivatorConfigException, SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2123.0), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(4998), Unit.cHz));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, new Scalar<>(0), Unit.kW)
                , result.get(NOT_USED_POWER_CODE));
    }
    
    @Test
    public void testDerivateReturns0ifProducedPowerIs0() throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(0.0), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(5012), Unit.cHz));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, new Scalar<>(0), Unit.kW)
                , result.get(NOT_USED_POWER_CODE));
    }
    
    @Test
    public void testDerivateReturnsProducedPowerIfFreqIsHihgLimit() throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2126), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(UPPER_LIMIT), LIMIT_UNIT));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, new Scalar<>(2.126), Unit.kW)
                , result.get(NOT_USED_POWER_CODE));
    }
    
    @Test
    public void testDerivateReturnsProducedPowerIfFreqIsAboveHihgLimit() throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2126), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(UPPER_LIMIT+0.1), LIMIT_UNIT));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, new Scalar<>(2.126), Unit.kW)
                , result.get(NOT_USED_POWER_CODE));
    }
    
    @Test
    public void testDerivateReturnsHalfProducedPowerIfFreqIsBetweenLimits() throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        //half -> Lower + ((upper - lower)*0.5)
        
        double halfFrequency=new BigDecimal(LOWER_LIMIT).add(
                new BigDecimal(UPPER_LIMIT)
                        .subtract(new BigDecimal(LOWER_LIMIT))
                        .multiply(new BigDecimal(0.5))
            ).doubleValue();
        
        
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2240), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(halfFrequency), LIMIT_UNIT));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        Measure unused=result.get(NOT_USED_POWER_CODE);
        assertEquals(Magnitude.POWER,unused.getMagnitude());
        unused=unused.convertUnits(Unit.W);
        assertEquals(1120.0,((Number)unused.getScalar().getValue()).doubleValue(),0.0001);
    }
    
    @Test
    public void testDerivateReturnsThreeTensProducedPowerIfFreqIsThreeTensBetweenLimits() throws SunnyBoyDerivatorAdapterException {
        when(config.getProducedPowerCode()).thenReturn(PRODUCED_POWER_CODE);
        when(config.getFrequencyCode()).thenReturn(FREQUENCY_CODE);
        when(config.getUnusedPowerCode()).thenReturn(NOT_USED_POWER_CODE);
        when(config.getFreqUnit()).thenReturn(LIMIT_UNIT);
        when(config.getUpperFreqLimit()).thenReturn(UPPER_LIMIT);
        when(config.getLowerFreqLimit()).thenReturn(LOWER_LIMIT);
        
        //half -> Lower + ((upper - lower)*0.5)
        
        double thirdFrequency=new BigDecimal(LOWER_LIMIT).add(
                new BigDecimal(UPPER_LIMIT)
                        .subtract(new BigDecimal(LOWER_LIMIT))
                        .multiply(new BigDecimal(0.3))
            ).doubleValue();
        
        
        
        Map<String, Measure> original=new HashMap<>();
        original.put(PRODUCED_POWER_CODE, 
                new Measure(Magnitude.POWER, new Scalar<>(2400), Unit.W));
        original.put(FREQUENCY_CODE, 
                new Measure(Magnitude.FREQUENCY, new Scalar<>(thirdFrequency), LIMIT_UNIT));
        
        Map<String,Measure> result=sut.derivate(original);
        
        assertTrue(result.containsKey(NOT_USED_POWER_CODE));
        Measure unused=result.get(NOT_USED_POWER_CODE);
        assertEquals(Magnitude.POWER,unused.getMagnitude());
        unused=unused.convertUnits(Unit.kW);
        assertEquals(0.720,((Number)unused.getScalar().getValue()).doubleValue(),0.00001);
    }

    
}

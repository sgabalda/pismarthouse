/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author gabalca
 */
public class GenericSunnyBoyDataAdapterTest {

    
    
    public GenericSunnyBoyDataAdapterTest() {
    }
    
    private GenericSunnyBoyDataAdapter sut;
    private SunnyBoyConfigResolver config;
    private Map<String, String> data;
    
    public static final String FREQUENCY_STRING_VALUE="5123";
    public static final double FREQUENCY_DOUBLE_VALUE=5123;
    public static final String PRODUCED_POWER_STRING_VALUE="12345";
    public static final double PRODUCED_POWER_DOUBLE_VALUE=12345;
        public static final String PRODUCED_POWER_CODE="ProducedPower";
    public static final String FREQUENCY_CODE="Frequency";
    
    @BeforeEach
    public void setUp() {
        data=new HashMap<>();
        data.put(FREQUENCY_CODE, FREQUENCY_STRING_VALUE);
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        config=mock(SunnyBoyConfigResolver.class);
        sut=new GenericSunnyBoyDataAdapter(config);
        sut.addLogger(mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    
    @Test
    public void canNotBeCreatedWithNullDataConfig() {
        Executable ex=()->{
            new GenericSunnyBoyDataAdapter(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"PowerSunnyBoyDataAdapter "
                + "can not be created with no config resolver");
    }
    
    
    @Test
    public void testGetMeasurementsReturnsMeasurementForProducedPower() throws SunnyBoyAdapterException {
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Measurements result=sut.getMeasurements(data);
        
        assertEquals(new Measure(Magnitude.POWER, 
                new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE), 
                Unit.W),result.getMeasure(PRODUCED_POWER_CODE));
        
    }
    
    @Test
    public void testGetMeasurementsReturnsMeasurementForFrequencyAndPower() throws SunnyBoyAdapterException {
        when(config.getDataUnits(FREQUENCY_CODE))
                .thenReturn(Unit.Hz);
        when(config.getDataMagnitude(FREQUENCY_CODE))
                .thenReturn(Magnitude.FREQUENCY);
        when(config.getDataType(FREQUENCY_CODE,FREQUENCY_STRING_VALUE))
                .thenReturn(new Scalar<>(FREQUENCY_DOUBLE_VALUE));
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        Measurements result=sut.getMeasurements(data);
        
        assertEquals(new Measure(Magnitude.FREQUENCY, 
                new Scalar<>(FREQUENCY_DOUBLE_VALUE), 
                Unit.Hz),result.getMeasure(FREQUENCY_CODE));
        
    }
    
    @Test
    public void testThrowsExceptionIfNotDataUnitsInConfigResolverForGivenCode() throws SunnyBoyAdapterException {
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(null);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Executable ex=()->{
            sut.getMeasurements(data);
        };
        SunnyBoyAdapterException result=assertThrows(SunnyBoyAdapterException.class, ex,"PowerSunnyBoyDataAdapter "
                + "must throw SunnyBoyAdapterException if the data Units is "
                + "not in the config resolver");
        assertTrue(result.getMessage().contains(PRODUCED_POWER_CODE));
        assertTrue(result.getMessage().contains("Unit"));
    }
    @Test
    public void testThrowsExceptionIfNotDataMagnitudeInConfigResolverForGivenCode() throws SunnyBoyAdapterException {
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(null);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Executable ex=()->{
            sut.getMeasurements(data);
        };
        SunnyBoyAdapterException result=assertThrows(SunnyBoyAdapterException.class, ex,"PowerSunnyBoyDataAdapter "
                + "must throw SunnyBoyAdapterException if the data Magnitude is "
                + "not in the config resolver");
        assertTrue(result.getMessage().contains(PRODUCED_POWER_CODE));
        assertTrue(result.getMessage().contains("Magnitude"));
    }
    @Test
    public void testThrowsExceptionIfNotDataTypeInConfigResolverForGivenCode() throws SunnyBoyAdapterException {
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(null);
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Executable ex=()->{
            sut.getMeasurements(data);
        };
        SunnyBoyAdapterException result=assertThrows(SunnyBoyAdapterException.class, ex,"PowerSunnyBoyDataAdapter "
                + "must throw SunnyBoyAdapterException if the data Type is "
                + "not in the config resolver");
        assertTrue(result.getMessage().contains(PRODUCED_POWER_CODE));
        assertTrue(result.getMessage().contains("Type"));
    }
    
    @Test
    public void testCallsTheDataDerivatorIfPresent() throws SunnyBoyAdapterException {
        SunnyBoyDataDerivator derivator=mock(SunnyBoyDataDerivator.class);
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        when(derivator.derivate(any(Map.class))).thenReturn(new HashMap<String,Measure>());
        
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        sut=new GenericSunnyBoyDataAdapter(config,derivator);
        sut.addLogger(mock(Logger.class));
        
        sut.getMeasurements(data);
        
        verify(derivator).derivate(any(Map.class));
        
    }
    
    @Test
    public void testMeasurementResultHasTheDervidedData() throws SunnyBoyAdapterException {
        SunnyBoyDataDerivator derivator=mock(SunnyBoyDataDerivator.class);
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Measure derivedMeasure=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Map<String,Measure> derived=new HashMap<>();
        derived.put("DerivedData",derivedMeasure);
        when(derivator.derivate(any(Map.class))).thenReturn(derived);
        
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        sut=new GenericSunnyBoyDataAdapter(config,derivator);
        sut.addLogger(mock(Logger.class));
        
        Measurements result=sut.getMeasurements(data);
        
        assertTrue(result.getAllMeasures().containsKey("DerivedData"));
        assertEquals(derivedMeasure, result.getMeasure("DerivedData"));
    }
    @Test
    public void testMeasurementResultHasTheOriginalData() throws SunnyBoyAdapterException {
        SunnyBoyDataDerivator derivator=mock(SunnyBoyDataDerivator.class);
        
        data=new HashMap<>();
        data.put(PRODUCED_POWER_CODE, PRODUCED_POWER_STRING_VALUE);
        
        Measure derivedMeasure=new Measure(Magnitude.ENERGY, new Scalar<>(10.0), Unit.Wh);
        Map<String,Measure> derived=new HashMap<>();
        derived.put("DerivedData",derivedMeasure);
        when(derivator.derivate(any(Map.class))).thenReturn(derived);
        
        when(config.getDataUnits(PRODUCED_POWER_CODE))
                .thenReturn(Unit.W);
        when(config.getDataMagnitude(PRODUCED_POWER_CODE))
                .thenReturn(Magnitude.POWER);
        when(config.getDataType(PRODUCED_POWER_CODE,PRODUCED_POWER_STRING_VALUE))
                .thenReturn(new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE));
        
        sut=new GenericSunnyBoyDataAdapter(config,derivator);
        sut.addLogger(mock(Logger.class));
        
        Measurements result=sut.getMeasurements(data);
        
        assertTrue(result.getAllMeasures().containsKey(PRODUCED_POWER_CODE));
        assertEquals(new Measure(Magnitude.POWER, 
                new Scalar<>(PRODUCED_POWER_DOUBLE_VALUE), 
                Unit.W),result.getMeasure(PRODUCED_POWER_CODE));
    }
    
}

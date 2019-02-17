/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import cat.casadepalla.casaEspavilada.core.SensorMeasurementException;
import java.util.logging.Logger;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author gabalca
 */
@ExtendWith(MockitoExtension.class)
public class GenericSunnyBoyWebInterfaceSensorTest {
    
    public GenericSunnyBoyWebInterfaceSensorTest() {
    }
    
    public static final String PRODUCED_POWER_CODE="ProducedPower";
    public static final String FREQUENCY_CODE="Frequency";

    @InjectMocks
    private GenericSunnyBoyWebInterfaceSensor sut;
    @Mock
    Logger logger;
    @Mock
    private Clock clock;
    @Mock
    private SunnyBoyWebClient client;
    @Mock
    private SunnyBoyDataAdapter adapter;
    @Mock
    private SunnyBoyConfigResolver config;
    private Map<String,String> data;
    
    @BeforeEach
    public void setUp() throws SunnyBoyWebClientException {
        data=new HashMap<>();
        data.put(FREQUENCY_CODE, "4999");
        data.put(PRODUCED_POWER_CODE, "12345");
        clock=Mockito.mock(Clock.class);
        sut=new GenericSunnyBoyWebInterfaceSensor(clock,client,adapter,config);
        
        sut.addLogger(logger);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCanNotCreateWithNullData () {
        Executable ex=()->{
            new GenericSunnyBoyWebInterfaceSensor(clock, client,adapter,null);
        };
        assertThrows(IllegalArgumentException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "needs a non empty data");
    }
    
    @Test
    public void testCanNotbeInitializedWithoutClock() throws Exception {
        Executable ex=()->{
            new GenericSunnyBoyWebInterfaceSensor(null, client,adapter,config);
        };
        assertThrows(IllegalArgumentException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "needs a non null clock");
    }
    
        @Test
    public void testCanNotbeInitializedWithoutClient() throws Exception {
        Executable ex=()->{
            new GenericSunnyBoyWebInterfaceSensor(clock, null,adapter,config);
        };
        assertThrows(IllegalArgumentException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "needs a non null client");
    }
    
        @Test
    public void testCanNotbeInitializedWithoutAdapter() throws Exception {
        Executable ex=()->{
            new GenericSunnyBoyWebInterfaceSensor(clock, client,null,config);
        };
        assertThrows(IllegalArgumentException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "needs a non null adapter");
    }
    
    /**
     * Test of measure method, of class GenericSunnyBoyWebInterfaceSensor.
     */
    @Test
    public void testMeasurePassesAdapterValuesToProcessor() throws SunnyBoyWebClientException, SensorMeasurementException, SunnyBoyAdapterException {
        
        Measurements measurements=mock(Measurements.class);
        
        when(config.getSunnyBoyDataCodesToRead()).thenReturn(new String[]{FREQUENCY_CODE,PRODUCED_POWER_CODE});
        
        when(adapter.getMeasurements(data)).thenReturn(measurements);
        when(client.getValues(FREQUENCY_CODE,PRODUCED_POWER_CODE)).thenReturn(data);
        when(clock.instant()).thenReturn(Instant.now());
        Processor p=mock(Processor.class);

        sut.measure(p);

        verify(p,times(1)).process(measurements);
        
    }
    @Test
    public void testMeasureCallsAdapter() throws SunnyBoyWebClientException, SensorMeasurementException, SunnyBoyAdapterException {
        
        Measurements measurements=mock(Measurements.class);
        
        when(config.getSunnyBoyDataCodesToRead()).thenReturn(new String[]{FREQUENCY_CODE,PRODUCED_POWER_CODE});
        
        when(adapter.getMeasurements(data)).thenReturn(measurements);
        when(client.getValues(FREQUENCY_CODE,PRODUCED_POWER_CODE)).thenReturn(data);
        when(clock.instant()).thenReturn(Instant.now());
        Processor p=mock(Processor.class);

        sut.measure(p);

        verify(adapter,times(1)).getMeasurements(data);
        
    }
    
    @Test
    public void testMeasurePropagatesClientException() throws SunnyBoyWebClientException, SensorMeasurementException {
        
        when(config.getSunnyBoyDataCodesToRead()).thenReturn(new String[]{FREQUENCY_CODE,PRODUCED_POWER_CODE});
        
        SunnyBoyWebClientException expected=new SunnyBoyWebClientException(new Exception());
        when(client.getValues(FREQUENCY_CODE,PRODUCED_POWER_CODE))
                .thenThrow(expected);
        when(clock.instant()).thenReturn(Instant.now());
        Processor p=mock(Processor.class);
        Executable ex=()->{
            sut.measure(p);
        };
        SensorMeasurementException exc=assertThrows(SensorMeasurementException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "must propagate client exception");
        assertEquals(expected,exc.getCause());

        
    }
    
    @Test
    public void testMeasurePropagatesAdapterException() throws SunnyBoyWebClientException, SensorMeasurementException, SunnyBoyAdapterException {
        
        when(config.getSunnyBoyDataCodesToRead()).thenReturn(new String[]{FREQUENCY_CODE,PRODUCED_POWER_CODE});
        
        SunnyBoyAdapterException expected=new SunnyBoyAdapterException(new Exception());
        
        when(adapter.getMeasurements(data)).thenThrow(expected);
        when(client.getValues(FREQUENCY_CODE,PRODUCED_POWER_CODE)).thenReturn(data);
        when(clock.instant()).thenReturn(Instant.now());
        Processor p=mock(Processor.class);
        Executable ex=()->{
            sut.measure(p);
        };
        SensorMeasurementException exc=assertThrows(SensorMeasurementException.class, ex,"GenericSunnyBoyWebInterfaceSensor "
                + "must propagate client exception");
        assertEquals(expected,exc.getCause());

        
    }

    /**
     * Test of isReady method, of class GenericSunnyBoyWebInterfaceSensor.
     */
    @Test
    public void testIsAlwaysReady() {
        assertTrue(sut.isReady());
    }

    /**
     * Test of getLastMeasuredTime method, of class GenericSunnyBoyWebInterfaceSensor.
     */
    @Test
    public void testGetLastMeasuredTime() throws SensorMeasurementException {
        Instant first = Instant.now();                  
        Instant second = first.plus(1,ChronoUnit.MINUTES); 
        when(clock.instant()).thenReturn(first, second);
        Processor p=Mockito.mock(Processor.class);
        sut.measure(p);
        Assertions.assertEquals(first.toEpochMilli(),sut.getLastMeasuredTime());
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author gabalca
 */
@ExtendWith(MockitoExtension.class)
public class CombinedSensorTest {
    
    public CombinedSensorTest() {
    }
    
    @Mock
    private Sensor sensor1;
    @Mock
    private Sensor sensor2;
    @Mock
    private Sensor sensor3;
    @Mock
    private Processor processor1;
    
    private CombinedSensor combined;
    
    @BeforeEach
    public void setUp() {        
        combined=new CombinedSensor(sensor1,sensor2,sensor3);
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void isReadyIfAllSensorsAreReady(){
        when(sensor1.isReady()).thenReturn(Boolean.TRUE);
        when(sensor2.isReady()).thenReturn(Boolean.TRUE);
        when(sensor3.isReady()).thenReturn(Boolean.TRUE);
        
        assertTrue(combined.isReady());
    }
    
    @Test
    public void isNotReadyIfOneSensorIsNotReady(){
        when(sensor1.isReady()).thenReturn(Boolean.TRUE);
        when(sensor2.isReady()).thenReturn(Boolean.FALSE);
        //when(sensor3.isReady()).thenReturn(Boolean.TRUE);
        
        assertFalse(combined.isReady());
        
    }
    
    @Test
    public void isNotReadyIfFirstSensorIsNotReady(){
        when(sensor1.isReady()).thenReturn(Boolean.FALSE);
        //when(sensor2.isReady()).thenReturn(Boolean.TRUE);
        //when(sensor3.isReady()).thenReturn(Boolean.TRUE);
        
        assertFalse(combined.isReady());
        
    }
    @Test
    public void isNotReadyIfLastSensorIsNotReady(){
        when(sensor1.isReady()).thenReturn(Boolean.TRUE);
        when(sensor2.isReady()).thenReturn(Boolean.TRUE);
        when(sensor3.isReady()).thenReturn(Boolean.FALSE);
        
        assertFalse(combined.isReady());
        
    }
    
    @Test
    public void initsAllSensors() throws SensorInitializationException{
        
        combined.init();
        
        verify(sensor1).init();
        verify(sensor2).init();
        verify(sensor3).init();
    }
    @Test
    public void cannotCreateACombinedWithNullSensors(){
        Executable ex=()->{
            CombinedSensor sut=new CombinedSensor((Sensor)null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"CombinedSensor is created with null inputs");
    }
    
    @Test
    public void cannotCreateACombinedWithoutSensors(){
        Executable ex=()->{
            CombinedSensor sut=new CombinedSensor();
        };
        
        assertThrows(IllegalArgumentException.class, ex,"CombinedSensor is created withhout inputs");
    }
    
    @Test
    public void cannotCreateACombinedWithNoSensors(){
        Executable ex=()->{
            CombinedSensor sut=new CombinedSensor(new Sensor[0]);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"CombinedSensor is created with one sensor inputs");
    }
    
    @Test
    public void cannotCreateWithNullSensor(){
        Executable ex=()->{
            CombinedSensor sut=new CombinedSensor(sensor1,sensor2,null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"CombinedSensor is created with one sensor null");
    }
    
    @Test
    public void lastMeasuredTimeIsWhenWasLastMeasured() throws SensorMeasurementException{
        
        Processor p=mock(Processor.class);
        
        long before=System.currentTimeMillis();
        combined.measure(p);
        long after=System.currentTimeMillis();
        
        assertTrue(before<=combined.getLastMeasuredTime(),"Must be after the sentence before");
        assertTrue(after>=combined.getLastMeasuredTime(),"Must be before the sentence after");
    }
    
    @Test
    public void callsMeasureOnAllSensors() throws SensorMeasurementException{
        
        combined.measure(processor1);
        
        verify(sensor1,times(1)).measure(processor1);
        verify(sensor2,times(1)).measure(processor1);
        verify(sensor3,times(1)).measure(processor1);
        
    }
    
}

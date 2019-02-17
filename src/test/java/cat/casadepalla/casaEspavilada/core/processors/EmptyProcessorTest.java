/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.processors;

import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 *
 * @author gabalca
 */
public class EmptyProcessorTest {
    
    public EmptyProcessorTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of process method, of class EmptyProcessor.
     */
    @Test
    public void testProcess() {
        
        Measurements m1=Mockito.mock(Measurements.class);
        
        Processor p1=new EmptyProcessor();
        
        p1.process(m1);
        
        verify(m1,never()).getAllMeasures();
        verify(m1,never()).getMeasure(ArgumentMatchers.anyString());
        verify(m1,never()).getMeasurementTime();
        verify(m1,never()).getMeasurementsNames();
        
    }
    
}

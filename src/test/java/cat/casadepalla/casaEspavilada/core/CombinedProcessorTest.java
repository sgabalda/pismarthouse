/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import java.util.Arrays;
import java.util.HashSet;
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
public class CombinedProcessorTest {
    
    public CombinedProcessorTest() {
    }
    
    
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Mock
    private Processor p1;
    
    @Mock
    private Processor p2;
    
    @Test
    public void canNotBeCreatedWithNullProcessors(){
        Executable ex=()->{
            CombinedProcessor sut=new CombinedProcessor(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"CombinedProcessor is created with null processors set");
    }
    
        @Test
    public void canNotBeCreatedWithEmptyProcessors(){
        Executable ex=()->{
            CombinedProcessor sut=new CombinedProcessor(new HashSet<>());
        };
        assertThrows(IllegalArgumentException.class, ex,"CombinedProcessor is created with empty processors set");
    }
    
    /**
     * Test of process method, of class CombinedProcessor.
     */
    @Test
    public void testProcess() {
        
        CombinedProcessor sut=new CombinedProcessor(new HashSet<>(Arrays.asList(p1,p2)));
        Measurements m1=mock(Measurements.class);
        sut.process(m1);
        
        verify(p1).process(m1);
        verify(p2).process(m1);
        
    }
    
}

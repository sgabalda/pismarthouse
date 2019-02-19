/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.processors;

import cat.casadepalla.casaEspavilada.core.Measure;
import java.util.logging.Logger;
import javax.management.RuntimeErrorException;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import cat.casadepalla.casaEspavilada.core.switches.MagnitudeBasedSwitch;


/**
 *
 * @author gabalca
 */
public class PowerBasedSwitchProcessorTest {
    
    public PowerBasedSwitchProcessorTest() {
    }
    
    private MagnitudeBasedSwitch PBswitch;
    private Measure treshold;
    
    private PowerBasedSwitchProcessor sut;
    
    @BeforeEach
    public void setUp() {
        PBswitch=mock(MagnitudeBasedSwitch.class);
        treshold=mock(Measure.class);
        sut=new PowerBasedSwitchProcessor(PBswitch,treshold);
        sut.addLogger(mock(Logger.class));
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCanNotBeCreatedWithoutPowerBasedSwith(){
        Executable ex=()->{
            sut=new PowerBasedSwitchProcessor(null,treshold);
            sut.addLogger(mock(Logger.class));
        };
        assertThrows(IllegalArgumentException.class, ex,"PowerBasedSwitchProcessor"
                + " is created with null PowerBased Switch");
    
    }
    
     @Test
    public void testCanNotBeCreatedWithouttreshold(){
        Executable ex=()->{
            sut=new PowerBasedSwitchProcessor(PBswitch,null);
             sut.addLogger(mock(Logger.class));
        };
        assertThrows(IllegalArgumentException.class, ex,"PowerBasedSwitchProcessor"
                + " is created with null PowerBased Switch");
    
    }
    
     @Test
    public void testCanNotBeCreatedWithNonComparableTreshold(){
        Executable ex=()->{
            when(treshold.compareTo(ArgumentMatchers.any())).thenThrow(RuntimeErrorException.class);
            sut=new PowerBasedSwitchProcessor(PBswitch,treshold);
            sut.addLogger(mock(Logger.class));
        };
        assertThrows(IllegalArgumentException.class, ex,"PowerBasedSwitchProcessor"
                + " is created with non comparable treshold");
    
    }
    /*
    @Test
    public void testOpensSwitchIfMagnitudeOverTreshold(){
        when(treshold.compareTo(ArgumentMatchers.any())).thenReturn(1);
        fail("Not yet implemented");
    }
    */
    
}

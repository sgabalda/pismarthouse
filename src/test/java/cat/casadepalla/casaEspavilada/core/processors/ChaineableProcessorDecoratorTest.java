/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.processors;

import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.ProcessedMeasurements;
import cat.casadepalla.casaEspavilada.core.Processor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class ChaineableProcessorDecoratorTest {
    
    public ChaineableProcessorDecoratorTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of process method, of class ChaineableProcessorDecorator.
     */
    @Test
    public void testProcess() {
        
    }
    
    @Test
    public void canNotBeCreatedWithNullNextProcessor(){
        Executable ex=()->{
            ChaineableProcessorDecoratorImpl sut=new ChaineableProcessorDecoratorImpl(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"ChaineableProcessorDecorator is created with null next processor");
    
    }

    public class ChaineableProcessorDecoratorImpl extends ChaineableProcessorDecorator {

        public ChaineableProcessorDecoratorImpl(Processor p) {
            super(p);
        }

        @Override
        public ProcessedMeasurements processThis(Measurements me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class ProductConverterTest {
    
    public ProductConverterTest() {
    }
    
    private ProductConverter converter;
    
    @BeforeEach
    public void setUp() {
        converter=new ProductConverter(10);
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of convertToReferenceUnit method, of class ProductConverter.
     */
    @Test
    public void testConvertToReferenceUnit() {
        
        Scalar<Number> origin=new Scalar<>(23.234);
        
        assertEquals(232.34, (double)converter.convertToReferenceUnit(origin).getValue());
        
    }
    
    @Test
    public void testCanNotConvertFromNullScalar() {
        Executable ex=()->{
            
            converter.convertToReferenceUnit(null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"COnverter must not accept a null value");    
    
    }
    
    @Test
    public void testConvertFromReferenceUnit(){
        Scalar<Number> origin=new Scalar<>(23.234);
        
        assertEquals(2.3234, (double)converter.convertFromReferenceUnit(origin).getValue());
    }
    
    @Test
    public void testCanNotConvertToNullScalar() {
        Executable ex=()->{
            converter.convertFromReferenceUnit(null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Converter must not accept a null value");    
    
    }
    
     @Test
    public void testDoesNotAccept0Ratio() {
        Executable ex=()->{
            new ProductConverter(0);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Converter must not accept 0 ratio");    
    
    }
    
}

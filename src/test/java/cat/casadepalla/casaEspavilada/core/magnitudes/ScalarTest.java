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
public class ScalarTest {
    
    public ScalarTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class Scalar.
     */
    @Test
    public void testGetValue() {
    }

    /**
     * Test of hashCode method, of class Scalar.
     */
    @Test
    public void testHashCode() {
    }

    /**
     * Test of equals method, of class Scalar.
     */
    @Test
    public void testEquals() {
        Scalar<Double> s1=new Scalar<>(22.56);
        Scalar<Double> s2=new Scalar<>(22.56);
        
        assertEquals(s2, s1);
    }
    
    @Test
    public void testEqualsEvenIfNotSameType() {
        Scalar<Integer> s1=new Scalar<>(22);
        Scalar<Double> s2=new Scalar<>(22.0);
        
        assertEquals(s2, s1);
    }
    
    @Test
    public void testnotEquals() {
        Scalar<Double> s1=new Scalar<>(22.56);
        Scalar<Double> s2=new Scalar<>(22.563);
        
        assertNotEquals(s2, s1);
    }
    
    @Test
    public void testNullValueNotAllowed(){
        Executable ex=()->{
            Scalar<Object> d=new Scalar<>(null);
        };
        
        assertThrows(IllegalArgumentException.class, ex,"Scalar must accept no null values");
        
    }
}

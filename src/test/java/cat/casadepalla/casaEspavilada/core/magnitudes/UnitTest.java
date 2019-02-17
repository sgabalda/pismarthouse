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

/**
 *
 * @author gabalca
 */
public class UnitTest {
    
    public UnitTest() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getConverter method, of class Unit.
     */
    @Test
    public void testGetConverter() {
        for(Unit u:Unit.values()){
            assertNotNull(u.getConverter(),"Unit "+u+" has no converter");
        }
    }
    
}

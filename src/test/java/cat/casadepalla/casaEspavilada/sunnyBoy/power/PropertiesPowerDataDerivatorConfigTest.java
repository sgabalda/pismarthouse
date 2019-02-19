/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.power;

import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 *
 * @author gabalca
 */
public class PropertiesPowerDataDerivatorConfigTest {
    
    public PropertiesPowerDataDerivatorConfigTest() {
    }
    
    BufferedWriter toFile;
    public final static String FILENAME="existent.properties";

    @BeforeEach
    public void setUp() throws IOException {
        toFile=new BufferedWriter(new FileWriter(FILENAME));
    }
    
    @AfterEach
    public void tearDown() throws IOException {
        toFile.close();
        File f=new File(FILENAME);
        if(f.exists()){
            f.delete();
        }
    }
    
    @Test
    public void testCanNotBeCreatedWithNullFile() throws Exception {
        Executable ex=()->{
            new PropertiesPowerDataDerivatorConfig(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"PropertiesPowerDataDerivatorConfig "
                + "can not be created with null file name");
    }
    
    @Test
    public void testCanNotBeCreatedWithEmptyFile() throws Exception {
        Executable ex=()->{
            new PropertiesPowerDataDerivatorConfig("");
        };
        assertThrows(IllegalArgumentException.class, ex,"PropertiesPowerDataDerivatorConfig "
                + "can not be created with no file name");
    }

    /**
     * Test of getFrequencyCode method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetFrequencyCode() throws Exception {
        String freqCode="frequency";
        toFile.append(PropertiesPowerDataDerivatorConfig.FREQ_CODE_PROP+"="+freqCode).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(freqCode,sut.getFrequencyCode());
    }

    /**
     * Test of getProducedPowerCode method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetProducedPowerCode() throws Exception {
        String prodPoCode="frequency";
        toFile.append(
                PropertiesPowerDataDerivatorConfig.PRODUCED_POWER_CODE_PROP
                        +"="+prodPoCode).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(prodPoCode,sut.getProducedPowerCode());
    }

    /**
     * Test of getUnusedPowerCode method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetUnusedPowerCode() throws Exception {
        String unPoCode="frequency";
        toFile.append(
                PropertiesPowerDataDerivatorConfig.UNUSED_POWER_CODE_PROP
                        +"="+unPoCode).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(unPoCode,sut.getUnusedPowerCode());
    }

    /**
     * Test of getUpperFreqLimit method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetUpperFreqLimit() throws Exception {
        String upperFreq="52.00";
        toFile.append(
                PropertiesPowerDataDerivatorConfig.FREQ_UPPER_PROP
                        +"="+upperFreq).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(52.00,sut.getUpperFreqLimit(),0.000001);
    }

    /**
     * Test of getLowerFreqLimit method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetLowerFreqLimit() throws Exception {
        String lowerFreq="50.00";
        toFile.append(
                PropertiesPowerDataDerivatorConfig.FREQ_LOWER_PROP
                        +"="+lowerFreq).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(50.00,sut.getLowerFreqLimit(),0.000001);
    }

    /**
     * Test of getFreqUnit method, of class PropertiesPowerDataDerivatorConfig.
     */
    @Test
    public void testGetFreqUnit() throws Exception {
        String unitFreq="Hz";
        toFile.append(
                PropertiesPowerDataDerivatorConfig.FREQ_UNIT_PROP
                        +"="+unitFreq).close();
        
        PropertiesPowerDataDerivatorConfig sut= new PropertiesPowerDataDerivatorConfig(FILENAME);
        
        assertEquals(Unit.Hz,sut.getFreqUnit());
    }
    
}

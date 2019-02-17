/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
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
public class PropertiesSunnyBoyConfigResolverTest {
    
    public PropertiesSunnyBoyConfigResolverTest() {
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
    public void testCanNotBeCreatedWithNullString() {
        Executable ex=()->{
            new PropertiesSunnyBoyConfigResolver(null);
        };
        assertThrows(IllegalArgumentException.class, ex,"PropertiesSunnyBoyConfigResolver "
                + "can not be created with no file name");
    }
    
    @Test
    public void testCanNotBeCreatedWithEmptyString() {
        Executable ex=()->{
            new PropertiesSunnyBoyConfigResolver("");
        };
        assertThrows(IllegalArgumentException.class, ex,"PropertiesSunnyBoyConfigResolver "
                + "can not be created with no file name");
    }
    
    @Test
    public void testCanNotBeCreatedWithNonExistentFile() {
        Executable ex=()->{
            new PropertiesSunnyBoyConfigResolver("nonexistent.properties");
        };
        assertThrows(IllegalArgumentException.class, ex,"PropertiesSunnyBoyConfigResolver "
                + "can not be created with no existent file");
    }
    
    @Test
    public void testCanBeCreatedWithExistentFile() {
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);

    }
    
    /**
     * Test of getLoginURL method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetLoginURL() throws IOException {
        String url="http://192.168.1.23/dyn/login.json";
        toFile.append(PropertiesSunnyBoyConfigResolver.LOGINURL_PROP+"="+url).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(url,sut.getLoginURL());
    }

    /**
     * Test of getDataUrl method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataUrl() throws IOException {
        String url="http://192.168.12.3/dyn/getValues.json";
        toFile.append(PropertiesSunnyBoyConfigResolver.DATAURL_PROP+"="+url).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(url,sut.getDataUrl());
    }

    /**
     * Test of getUsername method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetUsername() throws IOException {
        String user="usr";
        toFile.append(PropertiesSunnyBoyConfigResolver.USR_PROP+"="+user).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(user,sut.getUsername());
    }
    
    /**
     * Test of getDatacodes method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataCodes() throws IOException {
        String data="producedPower,frequency";
        toFile.append(PropertiesSunnyBoyConfigResolver.DATA_TO_READ_PROP+"="+data).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertArrayEquals(new String []{"producedPower","frequency"}
                ,sut.getSunnyBoyDataCodesToRead());
    }

    /**
     * Test of getPassword method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetPassword() throws IOException {
        String passwd="passwd";
        toFile.append(PropertiesSunnyBoyConfigResolver.PASS_PROP+"="+passwd).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(passwd,sut.getPassword());
    }

    /**
     * Test of getSessionParam method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetSessionParam() throws IOException {
        String sesparam="sid";
        toFile.append(PropertiesSunnyBoyConfigResolver.SESSPARAM_PROP+"="+sesparam).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(sesparam,sut.getSessionParam());
    }

    /**
     * Test of getDataCode method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataCode() throws IOException {
        String producedPowerCode="6100_40263F00";
        String sesparam="ProducedPower";
        toFile.append(
                PropertiesSunnyBoyConfigResolver.SUNNYBOY_NUMCODE_PROP_PREFIX
                        +sesparam+
                        "="+producedPowerCode).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(producedPowerCode,sut.getDataCode(sesparam));
    }

    /**
     * Test of getDataUnits method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataUnits() throws IOException {
        String producedPowerCode="ProducedPower";
        Unit result=Unit.W;
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_UNITS_PROP_PREFIX
                        +producedPowerCode+
                        "="+result.name()).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataUnits(producedPowerCode));
    }
    
    @Test
    public void testGetDataUnitsKw() throws IOException {
        String producedPowerCode="ProducedPower";
        Unit result=Unit.kW;
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_UNITS_PROP_PREFIX
                        +producedPowerCode+
                        "="+result.name()).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataUnits(producedPowerCode));
    }
    
    @Test
    public void testGetDataUnitscHz() throws IOException {
        String producedPowerCode="ProducedPower";
        Unit result=Unit.cHz;
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_UNITS_PROP_PREFIX
                        +producedPowerCode+
                        "="+result.name()).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataUnits(producedPowerCode));
    }

    /**
     * Test of getDataMagnitude method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataMagnitudePower() throws IOException {
        String producedPowerCode="ProducedPower";
        Magnitude result=Magnitude.POWER;
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_MAGNITUDE_PROP_PREFIX
                        +producedPowerCode+
                        "="+result.name()).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataMagnitude(producedPowerCode));
    }
    
    @Test
    public void testGetDataMagnitudeFrequency() throws IOException {
        String producedPowerCode="Frequency";
        Magnitude result=Magnitude.FREQUENCY;
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_MAGNITUDE_PROP_PREFIX
                        +producedPowerCode+
                        "="+result.name()).close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataMagnitude(producedPowerCode));
    }

    /**
     * Test of getDataType method, of class PropertiesSunnyBoyConfigResolver.
     */
    @Test
    public void testGetDataTypeFreqDouble() throws IOException {
        String producedPowerCode="Frequency";
        Scalar<Double> result=new Scalar<>(52.00);
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_TYPE_PROP_PREFIX
                        +producedPowerCode+
                        "=double").close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataType(producedPowerCode, "52.00"));
    }
    
    @Test
    public void testGetDataTypeFreqInt() throws IOException {
        String producedPowerCode="Frequency";
        Scalar<Integer> result=new Scalar<>(5127);
        toFile.append(
                PropertiesSunnyBoyConfigResolver.DATACODE_TYPE_PROP_PREFIX
                        +producedPowerCode+
                        "=int").close();
        
        PropertiesSunnyBoyConfigResolver sut= new PropertiesSunnyBoyConfigResolver(FILENAME);
        
        assertEquals(result,sut.getDataType(producedPowerCode, "5127"));
    }
    
}

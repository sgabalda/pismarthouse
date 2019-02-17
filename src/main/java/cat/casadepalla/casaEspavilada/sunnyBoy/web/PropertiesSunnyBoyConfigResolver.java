/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import cat.casadepalla.casaEspavilada.app.config.AbstractPropertiesConfig;
import cat.casadepalla.casaEspavilada.app.diProviders.ConfigValue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class PropertiesSunnyBoyConfigResolver 
        extends AbstractPropertiesConfig 
        implements SunnyBoyConfigResolver{
    
    public static final String LOGINURL_PROP="loginURL";
    public static final String DATAURL_PROP="dataURL";
    public static final String USR_PROP="username";
    public static final String PASS_PROP="password";
    public static final String SESSPARAM_PROP="sessionParam";
    public static final String SUNNYBOY_NUMCODE_PROP_PREFIX="sbnc.";
    public static final String DATACODE_UNITS_PROP_PREFIX="units.";
    public static final String DATACODE_MAGNITUDE_PROP_PREFIX="magnitude.";
    public static final String DATACODE_TYPE_PROP_PREFIX="type.";
    public static final String DATA_TO_READ_PROP="dataToRead";
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    @Inject
    public PropertiesSunnyBoyConfigResolver( @ConfigValue(code="sunnyboy.configfile") String filename) {
        super(filename);
    }

    @Override
    public String getLoginURL() {
        return getConfig().getString(LOGINURL_PROP);
    }

    @Override
    public String getDataUrl() {
        return getConfig().getString(DATAURL_PROP);
    }

    @Override
    public String getUsername() {
        return getConfig().getString(USR_PROP);
    }

    @Override
    public String getPassword() {
        
        return getConfig().getString(PASS_PROP);
    }

    @Override
    public String getSessionParam() {
       
        return getConfig().getString(SESSPARAM_PROP);
    }

    @Override
    public String getDataCode(String sunnyBoyNumericCode) {
        
        return getConfig().getString(SUNNYBOY_NUMCODE_PROP_PREFIX+sunnyBoyNumericCode);
    }

    @Override
    public Unit getDataUnits(String code) {
        
        return Unit.valueOf(getConfig().getString(DATACODE_UNITS_PROP_PREFIX+code));
    }

    @Override
    public Magnitude getDataMagnitude(String code) {
        
        return Magnitude.valueOf(getConfig().getString(DATACODE_MAGNITUDE_PROP_PREFIX+code));
    }

    @Override
    public Scalar getDataType(String code, String value) {
        String type=getConfig().getString(DATACODE_TYPE_PROP_PREFIX+code);
        if(null == type){
            logger.log(Level.WARNING,"Data type code for code {0} not configured in properties file",code);
            return null;
        }else switch (type) {
            case "double":
                return new Scalar<>(Double.parseDouble(value));
            case "int":
                return new Scalar<>(Integer.parseInt(value));
            default:
                logger.log(Level.WARNING,"Data type {0}  for code {1} is not known",new Object[]{type,code});
                return null;
        }
    }

    @Override
    public String[] getSunnyBoyDataCodesToRead() {
        
        return getConfig().getString(DATA_TO_READ_PROP).split(",");
        
    }
    
    
    
}

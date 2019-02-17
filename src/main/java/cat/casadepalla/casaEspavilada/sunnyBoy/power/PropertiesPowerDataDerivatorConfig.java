/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.power;

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
public class PropertiesPowerDataDerivatorConfig 
        extends AbstractPropertiesConfig
        implements PowerDataDerivatorConfig {
    
    public static final String FREQ_CODE_PROP="freq.code";
    public static final String PRODUCED_POWER_CODE_PROP="producedPower.code";
    public static final String UNUSED_POWER_CODE_PROP="unusedPower.code";
    public static final String FREQ_LOWER_PROP="freq.lower";
    public static final String FREQ_UPPER_PROP="freq.upper";
    public static final String FREQ_UNIT_PROP="freq.unit";
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    @Inject
    public PropertiesPowerDataDerivatorConfig(@ConfigValue(code="sunnyboy.configfile") String filename) {
        super(filename);
    }

    
    @Override
    public String getFrequencyCode() throws SunnyBoyDerivatorConfigException {
        return getConfig().getString(FREQ_CODE_PROP);
    }

    @Override
    public String getProducedPowerCode() throws SunnyBoyDerivatorConfigException {
        return getConfig().getString(PRODUCED_POWER_CODE_PROP);
    }

    @Override
    public String getUnusedPowerCode() throws SunnyBoyDerivatorConfigException {
        return getConfig().getString(UNUSED_POWER_CODE_PROP);
    }

    @Override
    public double getUpperFreqLimit() throws SunnyBoyDerivatorConfigException {
        try{
            return Double.parseDouble(getConfig().getString(FREQ_UPPER_PROP));
        }catch(NumberFormatException e){
            logger.log(Level.SEVERE,"The upper freq limit can not be converted "
                    + "to double: "+getConfig().getString(FREQ_UPPER_PROP),e);
            throw new SunnyBoyDerivatorConfigException("The Uppeer limit is not a double",e);
        }
    }

    @Override
    public double getLowerFreqLimit() throws SunnyBoyDerivatorConfigException {
        try{
            return Double.parseDouble(getConfig().getString(FREQ_LOWER_PROP));
        }catch(NumberFormatException e){
            logger.log(Level.SEVERE,"The LOWER freq limit can not be converted "
                    + "to double: "+getConfig().getString(FREQ_LOWER_PROP),e);
            throw new SunnyBoyDerivatorConfigException("The LOWER limit is not a double",e);
        }
    }

    @Override
    public Unit getFreqUnit() throws SunnyBoyDerivatorConfigException {
        return Unit.valueOf(getConfig().getString(FREQ_UNIT_PROP));
    }
    
}

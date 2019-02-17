/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.web;

import cat.casadepalla.casaEspavilada.core.CombinedMeasurements;
import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.Measurements;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class GenericSunnyBoyDataAdapter implements SunnyBoyDataAdapter{

    private SunnyBoyConfigResolver dataConfig;
    private SunnyBoyDataDerivator derivator;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}

    public GenericSunnyBoyDataAdapter(SunnyBoyConfigResolver dataConfig) {
        if(dataConfig==null)throw new IllegalArgumentException("Needs a non null data config resolver");
        this.dataConfig = dataConfig;
    }
    
    @Inject
    public GenericSunnyBoyDataAdapter(SunnyBoyConfigResolver dataConfig, SunnyBoyDataDerivator s) {
        this(dataConfig);
        this.derivator=s;
    }

    @Override
    public Measurements getMeasurements(Map<String, String> data) 
            throws SunnyBoyAdapterException{     
        logger.info("Getting the basic measures");
        Map<String, Measure> result=getBasicMeasures(data);
        if(derivator!=null){
            logger.info("There is a derivator. Getting all derivated measures.");
            result.putAll(derivator.derivate(result));
        }
        logger.info("Returning all measures.");
        return new CombinedMeasurements(result);

    }
    
    private Map<String, Measure> getBasicMeasures(Map<String, String> data) 
            throws SunnyBoyAdapterException{

        Map<String, Measure> result=new HashMap<>();
        for(String code:data.keySet()){
            logger.log(Level.FINE,"Getting data for code {0}",code);
            String value=data.get(code);
            
            Unit unit=dataConfig.getDataUnits(code);
            Magnitude magnitude=dataConfig.getDataMagnitude(code);
            Scalar<?> dataValue=dataConfig.getDataType(code,value);
            if(unit==null){
                logger.log(Level.WARNING,"Can not get unit for code {0}",code);
                throw new SunnyBoyAdapterException("No data Unit for data code "+code);
            }else if(magnitude==null){
                logger.log(Level.WARNING,"Can not get magnitude for code {0}",code);
                throw new SunnyBoyAdapterException("No data Magnitude for data code "+code);
            }else if(dataValue==null){
                logger.log(Level.WARNING,"Can not get type for code {0}",code);
                throw new SunnyBoyAdapterException("No data Type for data code "+code);
            }
            result.put(code, new Measure(
                    magnitude,
                    dataValue,
                    unit));
        }        
        return result;
    }
    
}

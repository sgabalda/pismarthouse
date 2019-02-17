/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.power;

import cat.casadepalla.casaEspavilada.core.Measure;
import cat.casadepalla.casaEspavilada.core.magnitudes.Magnitude;
import cat.casadepalla.casaEspavilada.core.magnitudes.Scalar;
import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyDataDerivator;
import cat.casadepalla.casaEspavilada.sunnyBoy.web.SunnyBoyDerivatorAdapterException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

/**
 *
 * @author gabalca
 */
public class PowerDataDerivator implements SunnyBoyDataDerivator{
    
    private PowerDataDerivatorConfig config;
    
    @Inject
    private Logger logger;
    void addLogger(Logger logger){ this.logger=logger;}
    
    @Inject
    public PowerDataDerivator(PowerDataDerivatorConfig config) {
        if(config==null){
            throw new IllegalArgumentException("can not create a "
                    + "PowerDataDerivator with null config");
        }
        this.config=config;
    }
    
    @Override
    public Map<String, Measure> derivate(Map<String, Measure> original)
        throws SunnyBoyDerivatorAdapterException{
        
        if(!original.containsKey(config.getProducedPowerCode())){
            logger.log(Level.WARNING, "The original data does not contain "
                    + "ProducedPower with code{0}", config.getProducedPowerCode());
           throw new SunnyBoyDerivatorAdapterException("No produced Power data"
                 + " with code "+config.getProducedPowerCode()+". Could not derivate unused power");
        }
        if(!original.containsKey(config.getFrequencyCode())){
            logger.log(Level.WARNING, "The original data does not contain "
                    + "frequency with code{0}", config.getFrequencyCode());
           throw new SunnyBoyDerivatorAdapterException("No frequency data"
                 + " with code "+config.getFrequencyCode()+". Could not derivate unused power");
        }
        
        Measure freq=original.get(config.getFrequencyCode());
        Measure producedPower=original.get(config.getProducedPowerCode());
        if(freq.getMagnitude()!=Magnitude.FREQUENCY){
            logger.log(Level.WARNING, "The data with code "
                    + "{0} is not of type frequency", config.getFrequencyCode());
            throw new SunnyBoyDerivatorAdapterException("The data with frequency code "
                    +config.getFrequencyCode()
                 + " Is not of type FREQUENCY: "+freq+". Could not derivate unused power");
        }
        Measure lowLimit=new Measure(Magnitude.FREQUENCY,
                new Scalar<>(config.getLowerFreqLimit()), 
                config.getFreqUnit());
        
        Measure upLimit=new Measure(Magnitude.FREQUENCY,
                new Scalar<>(config.getUpperFreqLimit()), 
                config.getFreqUnit());
        
        Scalar<Double> unusedScalar=null;
        Unit unusedUnit=Unit.W;
        if(freq.compareTo(lowLimit)<=0){
            //if the freq is below lower limit, then the not used power is 0
            logger.log(Level.INFO, "Frequency is under low limit. "
                    + "Unused power is 0");
            unusedScalar=new Scalar<>(0.0);
        }else if(
                producedPower.compareTo(
                        new Measure(Magnitude.POWER, new Scalar<>(0), Unit.W)
                )<=0){
            //if no produced power, the unused power is 0
            logger.log(Level.INFO, "Produced power is 0. "
                    + "Unused power is 0", config.getFrequencyCode());
            unusedScalar=new Scalar<>(0.0);
        }else if(freq.compareTo(upLimit)>=0){
            //if freq is equal or above up limit, then all produced power is not used
            logger.log(Level.INFO, "Freq is on upper limit. "
                    + "Unused power is produced power: {0}",producedPower);
            unusedScalar=producedPower.getScalar();
            unusedUnit=producedPower.getUnits();
        }else{
            //freq is between upper and lower
            logger.log(Level.INFO, "Freq is between upper and lower limit. "
                    + "Calculating unused power");
            
            Measure normalizedFreq=freq.convertUnits(config.getFreqUnit());
            
            BigDecimal freqBD=new BigDecimal(((Number)normalizedFreq.getScalar().getValue()).doubleValue());
            BigDecimal lowerBD=new BigDecimal(config.getLowerFreqLimit());
            BigDecimal upperBD=new BigDecimal(config.getUpperFreqLimit());
            
            //Ratio= (freq-lower)/(upper-lower)
            
            BigDecimal ratio=(freqBD.subtract(lowerBD).divide(upperBD.subtract(lowerBD)));
            
            //unused=produced*ratio
            
            BigDecimal unusedBD=new BigDecimal(((Number)producedPower.getScalar().getValue()).doubleValue());
            
            unusedBD=unusedBD.multiply(ratio);
            unusedScalar=new Scalar<>(unusedBD.doubleValue());
            unusedUnit=producedPower.getUnits();
        }
        
        
        Map<String,Measure> result=new HashMap<>();
        result.put(config.getUnusedPowerCode(),
                new Measure(Magnitude.POWER, unusedScalar, unusedUnit));
        
        
        return result;
    }
    
}

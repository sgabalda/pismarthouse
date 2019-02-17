/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app.diProviders;

import cat.casadepalla.casaEspavilada.app.config.AbstractPropertiesConfig;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author gabalca
 */
public class ConfigValuesProducer extends AbstractPropertiesConfig{

    public ConfigValuesProducer() {
        super("main.properties");
    }
    
    @Produces @ConfigValue
    public String getStringConfig (InjectionPoint ip){
        String code=ip.getAnnotated().getAnnotation(ConfigValue.class).code();
        return getConfig().getString(code);
    }
    
    @Produces @ConfigValue
    public Integer getIntegerConfig (InjectionPoint ip){
        String code=ip.getAnnotated().getAnnotation(ConfigValue.class).code();
        return Integer.parseInt(getConfig().getString(code));
    }
    
    @Produces @ConfigValue
    public Double getDoubleConfig (InjectionPoint ip){
        String code=ip.getAnnotated().getAnnotation(ConfigValue.class).code();
        return Double.parseDouble(getConfig().getString(code));
    }
    
    @Produces @ConfigValue
    public String [] getStringArrayConfig (InjectionPoint ip){
        String code=ip.getAnnotated().getAnnotation(ConfigValue.class).code();
        return getConfig().getString(code).split(",");
    }
    
}

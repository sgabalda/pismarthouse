/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app.config;

import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author gabalca
 */
public abstract class AbstractPropertiesConfig {
    
    private ImmutableConfiguration config;

    public AbstractPropertiesConfig(String filename) {
        if(filename==null || filename.isEmpty()){
            throw new IllegalArgumentException("Can not create a Properties "
                    + "config resolver without filename");
        }
        try{
            this.config = ConfigurationUtils
                    .unmodifiableConfiguration(
                            new Configurations().properties(filename));
        }catch(ConfigurationException e){
            throw new IllegalArgumentException(e);
        }
    }
    
    protected ImmutableConfiguration getConfig(){
        return config;
    }
    
    
}

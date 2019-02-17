/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.sunnyBoy.power;

import cat.casadepalla.casaEspavilada.core.magnitudes.Unit;

/**
 *
 * @author gabalca
 */
public interface PowerDataDerivatorConfig {
    String getFrequencyCode() throws SunnyBoyDerivatorConfigException;
    String getProducedPowerCode() throws SunnyBoyDerivatorConfigException;
    String getUnusedPowerCode() throws SunnyBoyDerivatorConfigException;
    double getUpperFreqLimit() throws SunnyBoyDerivatorConfigException;
    double getLowerFreqLimit() throws SunnyBoyDerivatorConfigException;
    Unit getFreqUnit() throws SunnyBoyDerivatorConfigException;
}

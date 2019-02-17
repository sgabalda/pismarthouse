/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.magnitudes;

/**
 *
 * @author gabalca
 */
public enum Unit {
    Hz(new ProductConverter(1)),
    cHz(new ProductConverter(100)),
    W(new ProductConverter(1)),
    kW(new ProductConverter(0.001)),
    Wh(new ProductConverter(1)),
    kWh(new ProductConverter(0.001));
    
    private final UnitConverter converter;

    private Unit(UnitConverter converter) {
        this.converter = converter;
    }
    
    protected UnitConverter getConverter(){
        return converter;
    }
    
}

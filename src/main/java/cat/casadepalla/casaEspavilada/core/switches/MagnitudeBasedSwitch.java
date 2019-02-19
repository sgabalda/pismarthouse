/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core.switches;

import cat.casadepalla.casaEspavilada.core.Measure;

/**
 *
 * @author gabalca
 */
public interface MagnitudeBasedSwitch extends Switch{
    public Measure getTreshold();
    public void setTreshold(Measure m);
    public boolean wouldTurnOnWithMeasure(Measure m);
    public void turnWithMeasure(Measure m);
}

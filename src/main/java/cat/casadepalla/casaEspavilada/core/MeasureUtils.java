/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.core;

import cat.casadepalla.casaEspavilada.core.utils.BasicMeasureUtils;

/**
 *
 * @author gabalca
 */
public interface MeasureUtils {
    public static MeasureUtils INSTANCE=new BasicMeasureUtils();
    /**
     * adds all given measures to the first one
     * @param m
     * @param measures
     * @return 
     */
    public Measure addMeasures(Measure m,Measure ...measures);
    /**
     * substracts all given measures to the first one
     * @param m
     * @param measures
     * @return 
     */
    public Measure substractMeasures(Measure m,Measure ...measures);

}

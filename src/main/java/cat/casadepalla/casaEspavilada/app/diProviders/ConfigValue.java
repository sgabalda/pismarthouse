/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.casadepalla.casaEspavilada.app.diProviders;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 *  Annotaton to hold config values and inject them at injection points.
 * IN INJECTION Points, the use must be:
 * @ConfigValue("AConfigcode") @Inject String field;
 * @ConfigValue("NotherConfigcode") @Inject int field;
 * @author gabalca
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
public @interface ConfigValue {
    @Nonbinding public String code() default "";
}

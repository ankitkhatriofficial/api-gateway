package org.khatri.sto.ambassador.annotations;

import java.lang.annotation.*;

/**
 * @author Ankit Khatri
 */

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenEndPoint {
}

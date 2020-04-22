package com.cang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Business parameter for a request.
 * Created by Liam on 2017/3/29.
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface ParamField {

  String value() default "";

}

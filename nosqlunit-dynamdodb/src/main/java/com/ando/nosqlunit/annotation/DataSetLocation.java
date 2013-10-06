package com.ando.nosqlunit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates classes that contain interaction methods with the database.
 * 
 * @author Zied ANDOLSI
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface DataSetLocation {

	/**
	 * Return value of DataSetLocation annotation.
	 * 
	 * @return data set location.
	 */
	public String value();

}

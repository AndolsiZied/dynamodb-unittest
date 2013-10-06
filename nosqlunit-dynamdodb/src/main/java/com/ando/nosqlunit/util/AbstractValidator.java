package com.ando.nosqlunit.util;

import java.util.Collection;
import java.util.Map;

/**
 * Abstract class validator.
 * 
 * @author andolsi zied
 * 
 */
public abstract class AbstractValidator {

	/**
	 * Testing if a common used object is null or empty.
	 * 
	 * @param object
	 *            object to validate
	 * @return Boolean value indicates if any object is null and if a string, a map or a collection is empty
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean isNullOrEmpty(Object object) {
		if (object == null) {
			return Boolean.TRUE;
		}
		if (object instanceof String) {
			return "".equals(((String) object).trim());
		}
		if (object instanceof Map) {
			return ((Map) object).isEmpty();
		}
		if (object instanceof Collection) {
			return ((Collection) object).isEmpty();
		}
		return Boolean.FALSE;
	}

}

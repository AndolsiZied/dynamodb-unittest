package com.ando.nosqlunit.exception.mapping;

import com.ando.nosqlunit.exception.DatabaseUnitException;

/**
 * The {@link DataMappingException} exception should be thrown when an exception occurs while trying to map data found
 * in JSON file to dynamodb objects.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class DataMappingException extends DatabaseUnitException {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = -81138177407772661L;

	/**
	 * Constructs a new <code>DataMappingException</code>.It only calls the corresponding parent constructor.
	 */
	public DataMappingException() {
		super();
	}

	/**
	 * Constructs a new <code>DataMappingException</code> with the specified detail message. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 */
	public DataMappingException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>DataMappingException</code> with the specified detail message and encapsulated exception.
	 * It only calls the corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 * @param e
	 *            throwable
	 */
	public DataMappingException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructs a new <code>DataMappingException</code> with the encapsulated exception. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param e
	 *            throwable
	 */
	public DataMappingException(Throwable e) {
		super(e);
	}

}

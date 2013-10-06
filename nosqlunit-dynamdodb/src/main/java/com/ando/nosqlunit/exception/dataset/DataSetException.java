package com.ando.nosqlunit.exception.dataset;

import com.ando.nosqlunit.exception.DatabaseUnitException;

/**
 * The {@link DataSetException} exception should be thrown when an exception occurs while trying to insert items into
 * database.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class DataSetException extends DatabaseUnitException {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = -7601131449918045343L;

	/**
	 * Constructs a new <code>DataSetException</code>.It only calls the corresponding parent constructor.
	 */
	public DataSetException() {
		super();
	}

	/**
	 * Constructs a new <code>DataSetException</code> with the specified detail message. It only calls the corresponding
	 * parent constructor.
	 * 
	 * @param msg
	 *            message
	 */
	public DataSetException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>DataSetException</code> with the specified detail message and encapsulated exception. It
	 * only calls the corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 * @param e
	 *            throwable
	 */
	public DataSetException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructs a new <code>DataSetException</code> with the encapsulated exception. It only calls the corresponding
	 * parent constructor.
	 * 
	 * @param e
	 *            throwable
	 */
	public DataSetException(Throwable e) {
		super(e);
	}

}

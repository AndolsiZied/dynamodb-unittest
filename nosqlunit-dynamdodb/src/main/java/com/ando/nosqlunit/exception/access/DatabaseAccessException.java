package com.ando.nosqlunit.exception.access;

import com.ando.nosqlunit.exception.DatabaseUnitException;

/**
 * The {@link DatabaseAccessException} exception should be thrown when an exception occurs while trying to connect to a
 * database.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class DatabaseAccessException extends DatabaseUnitException {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = -3230469605241541290L;

	/**
	 * Constructs a new <code>DatabaseAccessException</code>.It only calls the corresponding parent constructor.
	 */
	public DatabaseAccessException() {
		super();
	}

	/**
	 * Constructs a new <code>DatabaseAccessException</code> with the specified detail message. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 */
	public DatabaseAccessException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>DatabaseAccessException</code> with the specified detail message and encapsulated
	 * exception. It only calls the corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 * @param e
	 *            throwable
	 */
	public DatabaseAccessException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructs a new <code>DatabaseAccessException</code> with the encapsulated exception. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param e
	 *            throwable
	 */
	public DatabaseAccessException(Throwable e) {
		super(e);
	}

}

package com.ando.nosqlunit.exception;

/**
 * The {@link DatabaseUnitException} exception provides all the needed elements to create exceptions related to database
 * operations.
 * <p>
 * This exception is not directly instantiable.
 * 
 * @author Zied ANDOLSI
 * 
 */
public abstract class DatabaseUnitException extends Exception {

	/**
	 * Default serial version UID.
	 */
	private static final long serialVersionUID = 8388302639094019235L;

	/**
	 * Constructs a new <code>DatabaseUnitException</code>.It only calls the corresponding parent constructor.
	 */
	public DatabaseUnitException() {
		super();
	}

	/**
	 * Constructs a new <code>DatabaseUnitException</code> with the specified detail message. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 */
	public DatabaseUnitException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new <code>DatabaseUnitException</code> with the specified detail message and encapsulated exception.
	 * It only calls the corresponding parent constructor.
	 * 
	 * @param msg
	 *            message
	 * @param e
	 *            throwable
	 */
	public DatabaseUnitException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * Constructs a new <code>DatabaseUnitException</code> with the encapsulated exception. It only calls the
	 * corresponding parent constructor.
	 * 
	 * @param e
	 *            throwable
	 */
	public DatabaseUnitException(Throwable e) {
		super(e);
	}

}

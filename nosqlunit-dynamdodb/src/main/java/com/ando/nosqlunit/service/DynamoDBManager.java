package com.ando.nosqlunit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.ando.nosqlunit.di.CoreModule;
import com.ando.nosqlunit.exception.access.DatabaseAccessException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.michelboudreau.alternator.AlternatorDB;

/**
 * Class manager to start and stop dynamodb server.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class DynamoDBManager {

	/**
	 * {@link DynamoDBManager}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBManager.class);

	/**
	 * Dynamodb server.
	 */
	private AlternatorDB dataBase;

	/**
	 * Dynamodb client.
	 */
	private AmazonDynamoDB client;

	/**
	 * Default constructor.
	 */
	public DynamoDBManager() {
		dataBase = new AlternatorDB();
		Injector injector = Guice.createInjector(new CoreModule());
		client = injector.getInstance(AmazonDynamoDB.class);
	}

	/**
	 * Start server.
	 * 
	 * @throws DatabaseAccessException
	 *             thrown when an exception occurs while trying to connect to a database.
	 */
	public void startEngine() throws DatabaseAccessException {
		try {
			dataBase.start();
		} catch (Exception e) {
			LOGGER.error("error occured when trying to start datbase : " + e.getLocalizedMessage());
			throw new DatabaseAccessException(e);
		}
	}

	/**
	 * Stop server.
	 * 
	 * @throws DatabaseAccessException
	 *             thrown when an exception occurs while trying to connect to a database.
	 */
	public void stopEngine() throws DatabaseAccessException {
		try {
			dataBase.stop();
		} catch (Exception e) {
			LOGGER.error("error occured when trying to stop datbase : " + e.getLocalizedMessage());
			throw new DatabaseAccessException(e);
		}
	}

	/**
	 * Get dynamodb client.
	 * 
	 * @return {@link AmazonDynamoDB}
	 */
	public AmazonDynamoDB getDBClient() {
		return client;
	}

}

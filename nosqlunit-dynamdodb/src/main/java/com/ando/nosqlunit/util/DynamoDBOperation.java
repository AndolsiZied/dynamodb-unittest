package com.ando.nosqlunit.util;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.ando.nosqlunit.exception.mapping.DataMappingException;

/**
 * Class utilities for managing database. I contains all methods to get, create and delete tables thereby to insert
 * data.
 * 
 * @author Zied ANDOLSI
 * 
 */
public abstract class DynamoDBOperation {

	/**
	 * {@link DynamoDBOperation}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBOperation.class);

	/**
	 * Returns all existing tables in database.
	 * 
	 * @param client
	 *            client used to connect to database.
	 * @return list of table' name.
	 */
	public static List<String> getAllTables(AmazonDynamoDB client) {
		LOGGER.debug("starting getAllTables method...");
		ListTablesResult listTables = client.listTables();
		if (listTables != null) {
			return listTables.getTableNames();
		}
		return null;
	}

	/**
	 * Deletes tables whose name is received as parameter.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param tableNames
	 *            list of table' name
	 * @return true if all tables are deleted
	 */
	public static boolean deleteTables(AmazonDynamoDB client, List<String> tableNames) {
		LOGGER.debug("starting deleteTables method...");
		boolean result = true;
		for (String tableName : tableNames) {
			result = result && deleteTable(client, tableName);
		}
		return result;
	}

	/**
	 * Deletes table whose name is received as parameter.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param tableName
	 *            list of table' name
	 * @return true if table is deleted
	 */
	public static boolean deleteTable(AmazonDynamoDB client, String tableName) {
		LOGGER.debug("starting deleteTable method...");
		try {
			DeleteTableRequest request = new DeleteTableRequest().withTableName(tableName);
			DeleteTableResult result = client.deleteTable(request);
			return result != null;
		} catch (AmazonServiceException e) {
			LOGGER.error("error occurred when trying to delete table [ " + tableName + " ] : "
					+ e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Return list of tables described in the file received as parameter.
	 * 
	 * @param file
	 *            file
	 * @return list of tables
	 * @throws DataMappingException
	 *             thrown when error occurred while trying to map data found in <code>file</code> to dunamodb object
	 */
	public static List<TableDescription> getTablesFromFile(File file) throws DataMappingException {
		LOGGER.debug("starting getTablesFromFile method...");
		if (file == null) {
			LOGGER.error("file cannot be null");
			throw new IllegalArgumentException("file cannot be null");
		}
		if (!file.exists() || !file.isFile()) {
			LOGGER.error("specified  file [ " + file.getAbsolutePath() + " ] does not exist or is not a file");
			throw new IllegalArgumentException("specified  file [ " + file.getAbsolutePath()
					+ " ] does not exist or is not a file");
		}
		return JSONParser.parseInitFile(file);
	}

	/**
	 * Creates tables whose description is received as parameter.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param tables
	 *            list of {@link TableDescription}
	 * @return true if all tables are created
	 */
	public static boolean createTables(AmazonDynamoDB client, List<TableDescription> tables) {
		LOGGER.debug("starting createTables method...");
		boolean result = true;
		for (TableDescription tableDescription : tables) {
			result = result && createTable(client, tableDescription);
		}
		return result;

	}

	/**
	 * Creates table whose description is received as parameter.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param tableDescription
	 *            {@link TableDescription}
	 * @return true if table is created
	 */
	public static boolean createTable(AmazonDynamoDB client, TableDescription tableDescription) {
		try {
			CreateTableRequest createTableRequest = new CreateTableRequest();
			createTableRequest.setTableName(tableDescription.getTableName());
			createTableRequest.setAttributeDefinitions(tableDescription.getAttributeDefinitions());
			createTableRequest.setKeySchema(tableDescription.getKeySchema());
			ProvisionedThroughput provisionedthroughput =
					new ProvisionedThroughput().withReadCapacityUnits(
							tableDescription.getProvisionedThroughput().getReadCapacityUnits()).withWriteCapacityUnits(
							tableDescription.getProvisionedThroughput().getWriteCapacityUnits());
			createTableRequest.setProvisionedThroughput(provisionedthroughput);
			CreateTableResult result = client.createTable(createTableRequest);
			return result != null;
		} catch (AmazonServiceException e) {
			LOGGER.error("error occurred when trying to create table [ " + tableDescription.getTableName() + " ] : "
					+ e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Inserts data received in the <code>dataSetResourceFile</code>.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param dataSetResourceFile
	 *            data set resource file
	 * @return true if all items are inserted.
	 * @throws DataMappingException
	 *             thrown when error occurred while trying to map data found in <code>file</code> to dunamodb object
	 */
	public static boolean insertData(AmazonDynamoDB client, File dataSetResourceFile) throws DataMappingException {
		LOGGER.debug("starting insertData method...");
		if (dataSetResourceFile == null) {
			LOGGER.error("data set file cannot be null");
			throw new IllegalArgumentException("data set file cannot be null");
		}
		if (!dataSetResourceFile.exists() || !dataSetResourceFile.isFile()) {
			LOGGER.error("specified  file [ " + dataSetResourceFile.getAbsolutePath()
					+ " ] does not exist or is not a file");
			throw new IllegalArgumentException("specified  file [ " + dataSetResourceFile.getAbsolutePath()
					+ " ] does not exist or is not a file");
		}
		boolean result = true;
		List<PutItemRequest> items = JSONParser.parseDataSetFile(dataSetResourceFile);
		if (items != null && items.size() != 0) {
			for (PutItemRequest itemRequest : items) {
				result = result && insertItem(client, itemRequest);
			}
		}
		return result;
	}

	/**
	 * Inserts item received as parameter.
	 * 
	 * @param client
	 *            client used to connect to database
	 * @param itemRequest
	 *            item
	 * @return true if item is inserted.
	 */
	public static boolean insertItem(AmazonDynamoDB client, PutItemRequest itemRequest) {
		try {
			PutItemResult itemResult = client.putItem(itemRequest);
			return itemResult != null;
		} catch (AmazonServiceException e) {
			LOGGER.error("error occurred when trying to insert item in table [ " + itemRequest.getTableName() + " ] : "
					+ e.getLocalizedMessage());
			return false;
		}

	}

}

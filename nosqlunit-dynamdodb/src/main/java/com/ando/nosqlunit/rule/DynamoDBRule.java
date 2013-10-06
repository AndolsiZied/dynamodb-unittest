package com.ando.nosqlunit.rule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.ando.nosqlunit.annotation.DataSetLocation;
import com.ando.nosqlunit.exception.access.DatabaseAccessException;
import com.ando.nosqlunit.exception.dataset.DataSetException;
import com.ando.nosqlunit.exception.mapping.DataMappingException;
import com.ando.nosqlunit.service.DynamoDBManager;
import com.ando.nosqlunit.util.AbstractValidator;
import com.ando.nosqlunit.util.DynamoDBOperation;

/**
 * An implementation of @{TestRule} can be applied to a test database methods.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class DynamoDBRule implements TestRule {

	/**
	 * {@link DynamoDBRule}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBRule.class);

	/**
	 * Default init database file name.
	 */
	private static final String INIT_DB_FILE_NAME = "init_db.json";

	/**
	 * Default dataset file name suffix.
	 */
	private static final String DEFAULT_SUFFIX = "-dataset.json";

	/**
	 * Represents database actions to be taken at runtime in the course of running a JUnit test suite.
	 * 
	 * @author Zied ANDOLSI
	 * 
	 */
	public class DynamoDBRuleStatement extends Statement {

		/**
		 * The {@link Statement} to be modified.
		 */
		private Statement base;

		/**
		 * A {@link Description} describes a test which is to be run.
		 */
		private Description description;

		/**
		 * Dynamodb manager.
		 */
		private DynamoDBManager dbManager;

		/**
		 * Creates a new {@link DynamoDBRuleStatement} with the specified {@link Statement} and {@link Description}.
		 * 
		 * @param base
		 *            the {@link Statement} to be set.
		 * @param description
		 *            the {@link Description} to be set.
		 */
		public DynamoDBRuleStatement(Statement base, Description description) {
			this.base = base;
			this.description = description;
			this.dbManager = new DynamoDBManager();
		}

		/**
		 * {@inheritDoc}.
		 */
		public void evaluate() throws Throwable {
			initDB();
			base.evaluate();
			dbManager.stopEngine();
		}

		/**
		 * This method initialize database by deleting all existing tables then creating tables specified in
		 * <code>INIT_DB_FILE_NAME</code> then inserting data from dataset file.
		 * 
		 * @throws DatabaseAccessException
		 *             thrown when an exception occurs while trying to connect to a database.
		 * @throws DataSetException
		 *             thrown when an exception occurs while trying to insert items into database.
		 * @throws DataMappingException
		 *             thrown when an exception occurs while trying to map data found in JSON files to dynamodb objects
		 */
		private void initDB() throws DatabaseAccessException, DataSetException, DataMappingException {
			LOGGER.debug("starting initDB method...");
			// 1. starting databse server.
			dbManager.startEngine();

			// 2. getting table' name
			List<String> tableNames = DynamoDBOperation.getAllTables(dbManager.getDBClient());
			if (!AbstractValidator.isNullOrEmpty(tableNames)) {
				// 2.1 cleaning database
				boolean deleted = DynamoDBOperation.deleteTables(dbManager.getDBClient(), tableNames);
				if (!deleted) {
					LOGGER.error("error occurred when trying to reset database");
					throw new DatabaseAccessException("error occurred when trying to reset database");
				}
			}

			// 3. getting tables to create
			List<TableDescription> tables = getInitTables();
			if (tables == null) {
				LOGGER.error("no table found");
				throw new DatabaseAccessException("no table found in the init file");
			}

			// 4. creating tables
			boolean created = DynamoDBOperation.createTables(dbManager.getDBClient(), tables);
			if (!created) {
				LOGGER.error("error occurred when trying to create tables");
				throw new DatabaseAccessException("error occurred when trying to create tables");
			}

			// 5. getting location of dataset
			String dataSetResourcePath = getDataSetPath();
			if (dataSetResourcePath == null || dataSetResourcePath.trim().length() == 0) {
				LOGGER.info("[ " + description.getTestClass().getName()
						+ " ] does not have any data set, no data injection");
			} else {
				// 6. setting data
				URL url = description.getTestClass().getResource(dataSetResourcePath);
				if (url == null) {
					LOGGER.error("specified file [ " + dataSetResourcePath + " ] does not exist");
					throw new DataSetException("specified file [ " + dataSetResourcePath + " ] does not exist");
				}
				try {
					boolean inserted = DynamoDBOperation.insertData(dbManager.getDBClient(), new File(url.toURI()));
					if (!inserted) {
						LOGGER.error("error occurred when trying to insert items into database");
						throw new DataSetException("error occurred when trying to insert items into database");
					}
				} catch (URISyntaxException e) {
					LOGGER.error("error occurred when trying to get datset file : " + e.getLocalizedMessage());
					throw new DataSetException("error occurred when trying to insert items into database : "
							+ e.getLocalizedMessage());
				}
			}
		}

		/**
		 * Return dataset file path.
		 * 
		 * @return dataset path.
		 */
		private String getDataSetPath() {
			LOGGER.debug("starting getDataSetPathpath method...");
			String dataSetResourcePath = null;
			// getting the annotation of the test class
			DataSetLocation dataSetLocation = description.getTestClass().getAnnotation(DataSetLocation.class);
			if (dataSetLocation != null) {
				// found the annotation
				dataSetResourcePath = dataSetLocation.value();
				LOGGER.debug("annotated test, using data set [ " + dataSetResourcePath + " ]");
			} else {
				// no annotation, trying with the name of the test
				String packageName = description.getTestClass().getPackage().getName();
				String tempDsRes = packageName + packageName.substring(packageName.lastIndexOf('.'));
				tempDsRes = StringUtils.replace(tempDsRes, ".", "/");
				tempDsRes = "/" + tempDsRes + DEFAULT_SUFFIX;
				LOGGER.debug("no annotation, trying with the name of the test [ " + tempDsRes + " ]...");
				URL url = getClass().getResource(tempDsRes);
				if (url != null) {
					LOGGER.debug("detected default dataset [ " + tempDsRes + " ]");
					dataSetResourcePath = tempDsRes;
				} else {
					LOGGER.info("no default dataset");
				}
			}
			return dataSetResourcePath;
		}

		/**
		 * Return list of tables described in the init file.
		 * 
		 * @return list of tables.
		 * @throws DataMappingException
		 *             thrown when an exception occurs while trying to map data found in JSON files to dynamodb objects
		 */
		private List<TableDescription> getInitTables() throws DataMappingException {
			LOGGER.debug("starting getInitTables method...");
			String defaultPath = "/" + INIT_DB_FILE_NAME;
			URL url = description.getTestClass().getResource(defaultPath);
			if (url == null) {
				LOGGER.error("specified file [ " + defaultPath + " ] does not exist");
				throw new DataMappingException("specified file [ " + defaultPath + " ] does not exist");
			}
			File initDbFile;
			try {
				initDbFile = new File(url.toURI());
			} catch (URISyntaxException e) {
				LOGGER.error("error occurred when trying to get path of init database file : "
						+ e.getLocalizedMessage());
				throw new DataMappingException("error occurred when trying to get path of init database file : "
						+ e.getLocalizedMessage());
			}
			return DynamoDBOperation.getTablesFromFile(initDbFile);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	public Statement apply(Statement base, Description description) {
		return new DynamoDBRuleStatement(base, description);
	}

}

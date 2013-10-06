package com.ando.nosqlunit.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputDescription;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.ando.nosqlunit.exception.mapping.DataMappingException;

/**
 * Class utilities for parsing JSON file an returning dynamodb objects.
 * 
 * @author Zied ANDOLSI
 * 
 */
public abstract class JSONParser {

	/**
	 * {@link JSONParser}'s Logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONParser.class);

	/** table. */
	private static final String TABLE = "Table";

	/** table name. */
	private static final String TABLE_NAME = "TableName";

	/** attribute definitions. */
	private static final String ATT_DEFINITION = "AttributeDefinitions";

	/** attribute name. */
	private static final String ATT_NAME = "AttributeName";

	/** attribute type. */
	private static final String ATT_TYPE = "AttributeType";

	/** key schema. */
	private static final String KEY_SCHEMA = "KeySchema";

	/** key type. */
	private static final String KEY_TYPE = "KeyType";

	/** provisioned throughput. */
	private static final String PROVISIONED_THROUGHPUT = "ProvisionedThroughput";

	/** read capacity units. */
	private static final String READ_CAPACITY_UNITS = "ReadCapacityUnits";

	/** write capacity units. */
	private static final String WRITE_CAPACITY_UNITS = "WriteCapacityUnits";

	/**
	 * Parses JSON file which must contain tables to create.
	 * 
	 * @param initDbFile
	 *            file
	 * @return list of {@link TableDescription}.
	 * 
	 * @throws DataMappingException
	 *             thrown when error occurred while trying to map content of file to objects.
	 */
	public static List<TableDescription> parseInitFile(File initDbFile) throws DataMappingException {
		LOGGER.debug("starting parseInitFile method...");

		LOGGER.debug("parsing [ " + initDbFile.getAbsolutePath() + " ] file...");
		// 1. map content of the file to object
		ObjectMapper mapper = new ObjectMapper();
		Map initTables = null;
		try {
			initTables = mapper.readValue(initDbFile, Map.class);
		} catch (JsonParseException e) {
			LOGGER.error("error occurred when trying to parse the content of the file [ " + initDbFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to parse the content of the file [ "
					+ initDbFile.getName() + " ] : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			LOGGER.error("error occurred when trying to map the content of the file [ " + initDbFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to map the content of the file [ "
					+ initDbFile.getName() + " ] : " + e.getLocalizedMessage());
		} catch (IOException e) {
			LOGGER.error("error occurred when trying to parse the content of the file [ " + initDbFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to parse the content of the file [ "
					+ initDbFile.getName() + " ] : " + e.getLocalizedMessage());
		}

		if (AbstractValidator.isNullOrEmpty(initTables)) {
			LOGGER.error("file [ " + initDbFile.getAbsolutePath() + " ] must no be empty");
			throw new DataMappingException("file [ " + initDbFile.getAbsolutePath() + " ] must no be empty");
		}

		Object tablesObj = initTables.get(TABLE);
		if (!(tablesObj instanceof List<?>)) {
			LOGGER.error("expected list of tables not found in the file [ " + initDbFile.getName() + " ]");
			throw new DataMappingException("expected list of tables not found in the file [ " + initDbFile.getName()
					+ " ]");
		}

		// 2. getting list of tables
		List tablesMap = (List) tablesObj;
		if (AbstractValidator.isNullOrEmpty(tablesMap)) {
			LOGGER.error("list of tables must not be empty");
			throw new DataMappingException("list of tables must not be empty");
		}

		// 3. treatment table by table
		List<TableDescription> tables = new ArrayList<TableDescription>();
		for (Object tableObj : tablesMap) {
			if (!(tableObj instanceof Map)) {
				LOGGER.error("the table must be an instance of map");
				throw new DataMappingException("the table must be an instance of map");
			}
			Map tableMap = (Map) tableObj;
			if (AbstractValidator.isNullOrEmpty(tableMap)) {
				LOGGER.error("table content must no be empty");
				throw new DataMappingException("table content must no be empty");
			}

			// 3.1 getting table name
			Object tableNameObj = tableMap.get(TABLE_NAME);
			if (!(tableNameObj instanceof String)) {
				LOGGER.error("table name must be an insatnce of string");
				throw new DataMappingException("table name must be an insatnce of string");
			}
			String tableName = (String) tableNameObj;
			if (AbstractValidator.isNullOrEmpty(tableName)) {
				LOGGER.error("table name cannot be null or empty");
				throw new DataMappingException("table name cannot be null or empty");
			}
			TableDescription table = new TableDescription();
			table.setTableName(tableName);

			// 3.2 getting table's attributes
			Object attributesObj = tableMap.get(ATT_DEFINITION);
			if (!(attributesObj instanceof List<?>)) {
				LOGGER.error("table attributes must be an instance of list");
				throw new DataMappingException("table attributes must be an instance of list");
			}

			List attributes = (List) attributesObj;
			// 3.3 treatment attribute by attribute
			if (!AbstractValidator.isNullOrEmpty(attributes)) {
				List<AttributeDefinition> attributeList = new ArrayList<AttributeDefinition>();
				for (Object attributeObj : attributes) {
					if (!(attributeObj instanceof Map)) {
						LOGGER.error("an attribute must be an instance of map");
						throw new DataMappingException("an attribut must be an instance of map");
					}
					Map attributeMap = (Map) attributeObj;
					if (AbstractValidator.isNullOrEmpty(attributeMap)) {
						LOGGER.error("attribute content must not be empty");
						throw new DataMappingException("attribute content must not be empty");
					}

					// 3.3.1 getting attribute name
					Object attributeNameObj = attributeMap.get(ATT_NAME);
					if (!(attributeNameObj instanceof String)) {
						LOGGER.error("attribute name must be an insatnce of string");
						throw new DataMappingException("attribute name must be an insatnce of string");
					}
					String attributeName = (String) attributeNameObj;
					if (AbstractValidator.isNullOrEmpty(attributeName)) {
						LOGGER.error("attribute name cannot be null or empty");
						throw new DataMappingException("attribute name cannot be null or empty");
					}

					// 3.3.2 getting attribute type
					Object attributeTypeObj = attributeMap.get(ATT_TYPE);
					if (!(attributeTypeObj instanceof String)) {
						LOGGER.error("attribute type must be an insatnce of string");
						throw new DataMappingException("attribute type must be an insatnce of string");
					}
					String attributeType = (String) attributeTypeObj;
					if (AbstractValidator.isNullOrEmpty(attributeType)) {
						LOGGER.error("attribute type cannot be null or empty");
						throw new DataMappingException("attribute type cannot be null or empty");
					}
					ScalarAttributeType type = ScalarAttributeType.fromValue(attributeType);
					if (type == null) {
						LOGGER.error("unexpected attribute type found");
						throw new DataMappingException("unexpected attribute type found");
					}
					AttributeDefinition attribute = new AttributeDefinition();
					attribute.setAttributeName(attributeName);
					attribute.setAttributeType(type);
					attributeList.add(attribute);
				}
				table.setAttributeDefinitions(attributeList);
			}

			// 3.4. getting table's keys
			Object keysObj = tableMap.get(KEY_SCHEMA);
			if (!(keysObj instanceof List<?>)) {
				LOGGER.error("table keys must be an instance of list");
				throw new DataMappingException("table keys must be an instance of list");
			}

			List keys = (List) keysObj;
			// 3.5. treatment key by key
			if (!AbstractValidator.isNullOrEmpty(keys)) {
				List<KeySchemaElement> keyList = new ArrayList<KeySchemaElement>();
				for (Object keyObject : keys) {
					if (!(keyObject instanceof Map)) {
						LOGGER.error("the key schema must be an instance of map");
						throw new DataMappingException("the key schema must be an instance of map");
					}
					Map keyMap = (Map) keyObject;
					if (AbstractValidator.isNullOrEmpty(keyMap)) {
						LOGGER.error("key schema content must not be empty");
						throw new DataMappingException("key schema content must not be empty");
					}
					Object attributeNameObj = keyMap.get(ATT_NAME);
					if (!(attributeNameObj instanceof String)) {
						LOGGER.error("key name must be an insatnce of string");
						throw new DataMappingException("key name must be an insatnce of string");
					}
					String attributeName = (String) attributeNameObj;
					if (AbstractValidator.isNullOrEmpty(attributeName)) {
						LOGGER.error("key name cannot be null or empty");
						throw new DataMappingException("key name cannot be null or empty");
					}
					Object keyTypeObj = keyMap.get(KEY_TYPE);
					if (!(keyTypeObj instanceof String)) {
						LOGGER.error("key type must be an insatnce of string");
						throw new DataMappingException("key type must be an insatnce of string");
					}
					String keyType = (String) keyTypeObj;
					if (AbstractValidator.isNullOrEmpty(keyType)) {
						LOGGER.error("key type cannot be null or empty");
						throw new DataMappingException("key type cannot be null or empty");
					}
					KeyType type = KeyType.fromValue(keyType);
					if (type == null) {
						LOGGER.error("unexpected key type found");
						throw new DataMappingException("unexpected key type found");
					}
					KeySchemaElement key = new KeySchemaElement();
					key.setAttributeName(attributeName);
					key.setKeyType(type);
					keyList.add(key);
				}
				table.setKeySchema(keyList);
			}

			// 3.6. getting provisioned throughput
			Object throughputObj = tableMap.get(PROVISIONED_THROUGHPUT);
			if (!(throughputObj instanceof Map)) {
				LOGGER.error("the provisioned throughput must be an instance of map");
				throw new DataMappingException("the provisioned throughput must be an instance of map");
			}
			Map throughputMap = (Map) throughputObj;
			if (AbstractValidator.isNullOrEmpty(throughputMap)) {
				LOGGER.error("the provisioned throughput content cannot be empty");
				throw new DataMappingException("the provisioned throughput content cannot be empty");
			}

			// 3.6.1. getting read capacity unit
			Object readCapacityUnitsObj = throughputMap.get(READ_CAPACITY_UNITS);
			if (!(readCapacityUnitsObj instanceof Integer)) {
				LOGGER.error("the read capacity unit must be an instance of integer");
				throw new DataMappingException("the read capacity unit must be an instance of integer");
			}
			Integer readCapacityUnits = (Integer) readCapacityUnitsObj;
			if (readCapacityUnits == null || readCapacityUnits <= 0) {
				LOGGER.error("the read capacity unit cannot be null");
				throw new DataMappingException("the read capacity unit cannot be null");
			}
			Object writeCapacityUnitsObj = throughputMap.get(WRITE_CAPACITY_UNITS);
			if (!(writeCapacityUnitsObj instanceof Integer)) {
				LOGGER.error("the write capacity unit must be an instance of integer");
				throw new DataMappingException("the write capacity unit must be an instance of integer");
			}
			Integer writeCapacityUnits = (Integer) writeCapacityUnitsObj;
			if (writeCapacityUnits == null || writeCapacityUnits <= 0) {
				LOGGER.error("the write capacity unit cannot be null");
				throw new DataMappingException("the write capacity unit cannot be null");
			}
			ProvisionedThroughputDescription provisionedThroughput = new ProvisionedThroughputDescription();
			provisionedThroughput.setReadCapacityUnits(Long.valueOf(readCapacityUnits));
			provisionedThroughput.setWriteCapacityUnits(Long.valueOf(writeCapacityUnits));
			table.setProvisionedThroughput(provisionedThroughput);
			tables.add(table);
		}
		// 4. returning list of tables
		LOGGER.debug("returning [ " + tables.size() + "] tables...");
		return tables;
	}

	/**
	 * Parses JSON file which must contain data set.
	 * 
	 * @param dataSetFile
	 *            data set file
	 * @return list of {@link PutItemRequest}.
	 * 
	 * @throws DataMappingException
	 *             thrown when error occurred while trying to map content of file to objects.
	 */
	public static List<PutItemRequest> parseDataSetFile(File dataSetFile) throws DataMappingException {
		LOGGER.debug("starting parseDataSetFile method...");

		LOGGER.debug("parsing [ " + dataSetFile.getAbsolutePath() + " ] file...");
		// 1. map content of the file to object
		ObjectMapper mapper = new ObjectMapper();
		List<PutItemRequest> itemRequestList = null;
		Map<Object, Object> dataSetMap = null;
		try {
			dataSetMap = mapper.readValue(dataSetFile, Map.class);
		} catch (JsonParseException e) {
			LOGGER.error("error occurred when trying to parse the content of the file [ " + dataSetFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to parse the content of the file [ "
					+ dataSetFile.getName() + " ] : " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			LOGGER.error("error occurred when trying to map the content of the file [ " + dataSetFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to map the content of the file [ "
					+ dataSetFile.getName() + " ] : " + e.getLocalizedMessage());
		} catch (IOException e) {
			LOGGER.error("error occurred when trying to parse the content of the file [ " + dataSetFile.getName()
					+ " ] : " + e.getLocalizedMessage());
			throw new DataMappingException("error occurred when trying to parse the content of the file [ "
					+ dataSetFile.getName() + " ] : " + e.getLocalizedMessage());
		}

		// 2. parsing data set map
		if (!AbstractValidator.isNullOrEmpty(dataSetMap)) {
			itemRequestList = new ArrayList<PutItemRequest>();
			for (Map.Entry<Object, Object> entry : dataSetMap.entrySet()) {

				// 2.1. getting table name
				if (!(entry.getKey() instanceof String)) {
					LOGGER.error("table name must be an insatnce of string");
					throw new DataMappingException("table name must be an insatnce of string");
				}

				// 2.2. getting item's list
				if (!(entry.getValue() instanceof List)) {
					LOGGER.error("items must represented by a list");
					throw new DataMappingException("items must represented by a list");
				}
				List values = (List) entry.getValue();
				if (AbstractValidator.isNullOrEmpty(values)) {
					LOGGER.error("items list cannot be null or empty");
					throw new DataMappingException("items list cannot be null or empty");
				}

				// 2.3. treatment item by item
				for (Object object : values) {
					PutItemRequest itemRequest = new PutItemRequest();
					Map<String, AttributeValue> items = new HashMap<String, AttributeValue>();
					itemRequest.withTableName((String) entry.getKey());
					if (!(object instanceof Map)) {
						LOGGER.error("item's content must be an instance of map");
						throw new DataMappingException("item's content must be an instance of map");
					}
					for (Map.Entry<String, Object> subEntry : ((Map<String, Object>) object).entrySet()) {

						// 2.3.1. getting item name
						if (!(subEntry.getKey() instanceof String)) {
							LOGGER.error("item name must be an insatnce of string");
							throw new DataMappingException("item name must be an insatnce of string");
						}

						// 2.3.2. getting item value
						if (subEntry.getValue() instanceof List) {
							// 2.3.2.1 if item value is list then testing if the content of the list have elments wiht
							// the same type
							List attributes = (List) subEntry.getValue();
							boolean stringFlag = false;
							boolean numberFlag = false;
							boolean byteFlag = false;
							if (attributes.get(0) instanceof String) {
								stringFlag = true;
							} else {
								if (attributes.get(0) instanceof Number) {
									numberFlag = true;
								} else {
									if (attributes.get(0) instanceof ByteBuffer) {
										byteFlag = true;
									}
								}
							}
							for (int i = 1; i < attributes.size(); i++) {
								if (attributes.get(i) instanceof String && stringFlag) {
									continue;
								}
								if (attributes.get(i) instanceof Number && numberFlag) {
									continue;
								}
								if (attributes.get(i) instanceof ByteBuffer && byteFlag) {
									continue;
								}
								LOGGER.error("item's value list must have element with the same type");
								throw new DataMappingException("item's value list must have element with the same type");
							}
							if (stringFlag) {
								items.put((String) subEntry.getKey(), new AttributeValue().withSS(attributes));
							} else {
								if (numberFlag) {
									items.put((String) subEntry.getKey(), new AttributeValue().withNS(attributes));
								} else {
									items.put((String) subEntry.getKey(), new AttributeValue().withBS(attributes));
								}
							}

						} else {
							if (subEntry.getValue() instanceof String) {
								items.put((String) subEntry.getKey(),
										new AttributeValue().withS((String) subEntry.getValue()));
							} else {
								if (subEntry.getValue() instanceof Number) {
									items.put((String) subEntry.getKey(),
											new AttributeValue().withN(((Number) subEntry.getValue()).toString()));
								} else {
									if (subEntry.getValue() instanceof ByteBuffer) {
										items.put((String) subEntry.getKey(),
												new AttributeValue().withB((ByteBuffer) subEntry.getValue()));
									}
								}
							}
						}
						itemRequest.withItem(items);
					}
					itemRequestList.add(itemRequest);
				}

			}
		}

		// 3. returning itmess
		LOGGER.debug("returning items...");
		return itemRequestList;
	}

}

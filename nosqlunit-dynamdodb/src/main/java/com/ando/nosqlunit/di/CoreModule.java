package com.ando.nosqlunit.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.michelboudreau.alternatorv2.AlternatorDBClientV2;

/**
 * Central class to provide implementation for the application.
 * 
 * @author Zied ANDOLSI
 * 
 */
public class CoreModule extends AbstractModule {

	/**
	 * {@inheritDoc}.
	 */
	protected void configure() {
	}

	/**
	 * Get DynamoDB mapper.
	 * 
	 * @param dynamoDBClient
	 *            dynamoDB client
	 * @return {@link DynamoDBMapper}
	 */
	@Inject
	@Provides
	public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB dynamoDBClient) {
		return new DynamoDBMapper(dynamoDBClient);
	}

	/**
	 * Get dynamoDB client.
	 * 
	 * @return {@link AmazonDynamoDB}
	 */
	@Provides
	public AmazonDynamoDB getAmazonDynamoDB() {
		return new AlternatorDBClientV2();
	}

}

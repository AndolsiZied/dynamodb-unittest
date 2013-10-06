package com.ando.unitdemo.di;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ando.nosqlunit.service.DynamoDBManager;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new DAOModule());
	}

	@Inject
	@Provides
	public DynamoDBMapper getDynamoDBMapper(AmazonDynamoDB dynamoDBClient) {
		return new DynamoDBMapper(dynamoDBClient);
	}

	@Provides
	public AmazonDynamoDB getAmazonDynamoDB() {
		return new DynamoDBManager().getDBClient();
	}

}

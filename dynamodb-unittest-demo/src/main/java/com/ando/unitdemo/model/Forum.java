package com.ando.unitdemo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "forum")
public class Forum {

	private String name;
	private String category;
	private Integer threads;
	private Integer message;
	private Integer views;

	@DynamoDBHashKey(attributeName = "Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @DynamoDBAttribute(attributeName = "Category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// @DynamoDBAttribute(attributeName = "Threads")
	public Integer getThreads() {
		return threads;
	}

	public void setThreads(Integer threads) {
		this.threads = threads;
	}

	// @DynamoDBAttribute(attributeName = "Message")
	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	// @DynamoDBAttribute(attributeName = "Views")
	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}
}

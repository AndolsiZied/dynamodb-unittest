package com.ando.unitdemo.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Thread")
public class Thread {

	private String forumName;
	private String subject;
	private String message;
	private String lastPostedBy;
	private String lastPostedDateTime;
	private Integer views;
	private String replies;
	private String answered;
	private List<String> tags;

	@DynamoDBHashKey(attributeName = "ForumName")
	public String getForumName() {
		return forumName;
	}

	public void setForumName(String forumName) {
		this.forumName = forumName;
	}

	@DynamoDBRangeKey(attributeName = "Subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@DynamoDBAttribute(attributeName = "Message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@DynamoDBAttribute(attributeName = "LastPostedBy")
	public String getLastPostedBy() {
		return lastPostedBy;
	}

	public void setLastPostedBy(String lastPostedBy) {
		this.lastPostedBy = lastPostedBy;
	}

	@DynamoDBAttribute(attributeName = "LastPostedDateTime")
	public String getLastPostedDateTime() {
		return lastPostedDateTime;
	}

	public void setLastPostedDateTime(String lastPostedDateTime) {
		this.lastPostedDateTime = lastPostedDateTime;
	}

	@DynamoDBAttribute(attributeName = "Views")
	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@DynamoDBAttribute(attributeName = "Replies")
	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	@DynamoDBAttribute(attributeName = "Answered")
	public String getAnswered() {
		return answered;
	}

	public void setAnswered(String answered) {
		this.answered = answered;
	}

	@DynamoDBAttribute(attributeName = "Tags")
	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}

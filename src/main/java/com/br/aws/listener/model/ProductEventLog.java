package com.br.aws.listener.model;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.br.aws.listener.enuns.EventTypeEnum;

@DynamoDBTable(tableName = "product-events")
public class ProductEventLog {

	public ProductEventLog() {
	}

	@Id
	private ProductEventKey productEventKey;

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "eventType")
	private EventTypeEnum eventType;

	@DynamoDBAttribute(attributeName = "productId")
	private long productId;

	@DynamoDBAttribute(attributeName = "username")
	private String username;

	@DynamoDBAttribute(attributeName = "messageId")
	private String messageId;

	@DynamoDBAttribute(attributeName = "timestamp")
	private long timestamp;

	@DynamoDBAttribute(attributeName = "ttl")
	private long ttl;

	@DynamoDBHashKey(attributeName = "pk")
	public String getPk() {
		return this.productEventKey != null ? this.productEventKey.getPk() : null;
	}

	public void setPk(String pk) {
		if (this.productEventKey == null) {
			this.productEventKey = new ProductEventKey();
		}

		this.productEventKey.setPk(pk);
	}

	@DynamoDBRangeKey(attributeName = "sk")
	public String getSk() {
		return this.productEventKey != null ? this.productEventKey.getSk() : null;
	}

	public void setSk(String sk) {
		if (this.productEventKey == null) {
			this.productEventKey = new ProductEventKey();
		}

		this.productEventKey.setSk(sk);
	}

	public EventTypeEnum getEventType() {
		return eventType;
	}

	public void setEventType(EventTypeEnum eventType) {
		this.eventType = eventType;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}

package com.br.aws.listener.model;

import com.br.aws.listener.enuns.EventTypeEnum;

public class ProductEventLogDTO {

	private final String code;
	private final EventTypeEnum eventType;
	private final long productId;
	private final String username;
	private final long timestamp;

	public ProductEventLogDTO(ProductEventLog productEventLog) {
		this.code = productEventLog.getPk();
		this.eventType = productEventLog.getEventType();
		this.productId = productEventLog.getProductId();
		this.username = productEventLog.getUsername();
		this.timestamp = productEventLog.getTimestamp();
	}

	public String getCode() {
		return code;
	}

	public EventTypeEnum getEventType() {
		return eventType;
	}

	public long getProductId() {
		return productId;
	}

	public String getUsername() {
		return username;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

package com.br.aws.listener.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.br.aws.listener.model.EnvelopeDTO;
import com.br.aws.listener.model.ProductEventDTO;
import com.br.aws.listener.model.ProductEventLog;
import com.br.aws.listener.model.SnsMessageDTO;
import com.br.aws.listener.repositories.ProductEventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductConsumerService {

	private static final Logger log = LoggerFactory.getLogger(ProductConsumerService.class);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductEventLogRepository productEventLogRepository;

	@JmsListener(destination = "${aws.sqs.queue.product.name}")
	public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

		final SnsMessageDTO snsMessage = this.objectMapper.readValue(textMessage.getText(), SnsMessageDTO.class);

		final EnvelopeDTO envelope = objectMapper.readValue(snsMessage.getMessage(), EnvelopeDTO.class);

		final ProductEventDTO productEvent = objectMapper.readValue(envelope.getData(), ProductEventDTO.class);

		log.info("Product event received - Event: {} - ProductId: {} - MessageId: {}", envelope.getEventType(),
				productEvent.getProductId(), snsMessage.getMessageId());

		final ProductEventLog productEventLog = buildProductEventLog(envelope, productEvent, snsMessage);
		productEventLogRepository.save(productEventLog);
	}

	private ProductEventLog buildProductEventLog(EnvelopeDTO envelope, ProductEventDTO productEvent, SnsMessageDTO snsMessage) {
		long timestamp = Instant.now().toEpochMilli();

		ProductEventLog productEventLog = new ProductEventLog();
		productEventLog.setPk(productEvent.getCode());
		productEventLog.setSk(envelope.getEventType() + "_" + timestamp);
		productEventLog.setEventType(envelope.getEventType());
		productEventLog.setProductId(productEvent.getProductId());
		productEventLog.setMessageId(snsMessage.getMessageId());
		productEventLog.setUsername(productEvent.getUsername());
		productEventLog.setTimestamp(timestamp);
		productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());

		return productEventLog;
	}

}

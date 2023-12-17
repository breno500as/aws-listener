package com.br.aws.listener.service;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.br.aws.listener.model.EnvelopeDTO;
import com.br.aws.listener.model.ProductEventDTO;
import com.br.aws.listener.model.SnsMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductConsumerService {

	private static final Logger log = LoggerFactory.getLogger(ProductConsumerService.class);

	@Autowired
	private ObjectMapper objectMapper;

	@JmsListener(destination = "${aws.sqs.queue.product.name}")
	public void receiveProductEvent(TextMessage textMessage) throws JMSException, IOException {

		SnsMessageDTO snsMessage = this.objectMapper.readValue(textMessage.getText(), SnsMessageDTO.class);

		EnvelopeDTO envelope = objectMapper.readValue(snsMessage.getMessage(), EnvelopeDTO.class);

		ProductEventDTO productEvent = objectMapper.readValue(envelope.getData(), ProductEventDTO.class);

		log.info("Product event received - Event: {} - ProductId: {} - MessageId: {}", envelope.getEventType(),
				productEvent.getProductId(), snsMessage.getMessageId());
	}

}

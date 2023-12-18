package com.br.aws.listener.api;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.aws.listener.model.ProductEventLogDTO;
import com.br.aws.listener.repositories.ProductEventLogRepository;

@RestController
@RequestMapping("/api/product-events")
public class ProductEventLogAPI {

	@Autowired
	private ProductEventLogRepository productEventLogRepository;

	@GetMapping
	public List<ProductEventLogDTO> getAllEvents() {
		return StreamSupport.stream(productEventLogRepository.findAll().spliterator(), false)
				.map(ProductEventLogDTO::new).collect(Collectors.toList());
	}

	@GetMapping("/{code}")
	public List<ProductEventLogDTO> findByCode(@PathVariable String code) {
		return productEventLogRepository.findAllByPk(code).stream().map(ProductEventLogDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/{code}/{event}")
	public List<ProductEventLogDTO> findByCodeAndEventType(@PathVariable String code, @PathVariable String event) {
		return productEventLogRepository.findAllByPkAndSkStartsWith(code, event).stream().map(ProductEventLogDTO::new)
				.collect(Collectors.toList());
	}

}

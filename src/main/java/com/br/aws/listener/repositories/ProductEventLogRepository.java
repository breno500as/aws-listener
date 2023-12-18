package com.br.aws.listener.repositories;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.br.aws.listener.model.ProductEventKey;
import com.br.aws.listener.model.ProductEventLog;

@EnableScan
public interface ProductEventLogRepository extends CrudRepository <ProductEventLog, ProductEventKey>{

    List<ProductEventLog> findAllByPk(String code);

    List<ProductEventLog> findAllByPkAndSkStartsWith(String code, String eventType);
}

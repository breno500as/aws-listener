package com.br.aws.listener.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.br.aws.listener.repositories.ProductEventLogRepository;

@Configuration
@Profile("localhost")
@EnableDynamoDBRepositories(basePackageClasses = ProductEventLogRepository.class)
public class AwsLocalStackDynamoDB {

	private static final Logger log = LoggerFactory.getLogger(AwsLocalStackDynamoDB.class);

	private AmazonDynamoDB amazonDynamoDB;

	public AwsLocalStackDynamoDB() {
		this.amazonDynamoDB = AmazonDynamoDBClient.builder().withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.US_EAST_1.getName()))
				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();

		DynamoDB dynamoDB = new DynamoDB(this.amazonDynamoDB);

		final List<AttributeDefinition> attributes = new ArrayList<>();
		attributes.add(new AttributeDefinition().withAttributeName("pk").withAttributeType(ScalarAttributeType.S));
		attributes.add(new AttributeDefinition().withAttributeName("sk").withAttributeType(ScalarAttributeType.S));

		final List<KeySchemaElement> schemaElements = new ArrayList<>();
		schemaElements.add(new KeySchemaElement().withAttributeName("pk").withKeyType(KeyType.HASH));
		schemaElements.add(new KeySchemaElement().withAttributeName("sk").withKeyType(KeyType.RANGE));

		final CreateTableRequest createTableRequest = new CreateTableRequest().withTableName("product-events")
				.withKeySchema(schemaElements).withBillingMode(BillingMode.PAY_PER_REQUEST)
				.withAttributeDefinitions(attributes);

		Table t = dynamoDB.createTable(createTableRequest);

		try {
			t.waitForActive();
		} catch (Exception e) {
			log.error("Erro ao ativar a tabela {}", e.getMessage(), e);
		}
	}

	@Value("${aws.region}")
	private String awsRegion;

	@Bean
	@Primary
	public DynamoDBMapperConfig dynamoDBMapperConfig() {
		return DynamoDBMapperConfig.DEFAULT;
	}

	@Bean
	@Primary
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
		return new DynamoDBMapper(amazonDynamoDB, config);
	}

	@Bean
	@Primary
	public AmazonDynamoDB amazonDynamoDB() {
		return this.amazonDynamoDB;
	}

}

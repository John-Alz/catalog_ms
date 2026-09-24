package com.eatshub.catalog.application.config.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableReactiveMongoRepositories("com.eatshub.catalog")
@PropertySource("classpath:mongo-conection.properties")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Value("${mongodb.database}")
    private String database;

    @Value("${mongodb.username}")
    private String userName;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.authentication-database}")
    private String authSource;

    @Value("${mongodb.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${mongodb.minPoolSize}")
    private Integer minPoolSize;

    @Value("${mongodb.maxConnectionLifeTime}")
    private Long maxConnectionLifeTime;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        final ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%d/%s", host, port, database));
        final MongoCredential credential = MongoCredential.createCredential(userName, authSource, password.toCharArray());
        final MongoClientSettings settings = MongoClientSettings.builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyConnectionString(connectionString)
                .credential(credential)
                .applyToConnectionPoolSettings(poolSettings ->
                        poolSettings.maxSize(maxPoolSize)
                                .minSize(minPoolSize)
                                .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS)
                        )
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, getDatabaseName());
    }

}

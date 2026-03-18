package com.attendance.backend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        return connectionString.getDatabase();
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}

package dev.hutchison.rsvp.service;

import com.mongodb.client.MongoClient;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SuppressWarnings("unused")
@TestConfiguration
@EnableAutoConfiguration(exclude = {MongoDataAutoConfiguration.class, MongoAutoConfiguration.class})
public class AppTestConfiguration {
    @Bean("mongoTemplate")
    MongoTemplate mongoTemplate() {
        MongoClient mockMongoClient = Mockito.mock(MongoClient.class, Mockito.RETURNS_MOCKS);
        return new MongoTemplate(mockMongoClient, "test");
    }
}

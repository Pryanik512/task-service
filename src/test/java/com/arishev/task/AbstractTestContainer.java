package com.arishev.task;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

public class AbstractTestContainer {

    static PostgreSQLContainer<?> postgres;

    static KafkaContainer kafka;


    static {

        postgres = new PostgreSQLContainer<>("postgres:14.8-alpine3.18")
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test");

        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.4"))
                .withEmbeddedZookeeper()
                .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "BROKER:PLAINTEXT,PLAINTEXT:PLAINTEXT")
                .withEnv("KAFKA_INTER_BROKER_LISTENER_NAME", "BROKER")
                .withEnv("KAFKA_BROKER_ID", "1")
                .withEnv("KAFKA_BROKER_ID", "1")
                .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1");

        Startables.deepStart(Stream.of(postgres, kafka)).join();

    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("task-service.kafka.servers", kafka::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

}

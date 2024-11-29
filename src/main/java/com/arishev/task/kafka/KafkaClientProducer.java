package com.arishev.task.kafka;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;


@Slf4j
@AllArgsConstructor
public class KafkaClientProducer {

    private KafkaTemplate template;

    public void sendTo(String topic, Object o) {

        try {
            template.send(topic, o);
            template.flush();
        } catch (Exception e) {
            log.error("Kafka Producer error:", e);
        }
    }
}

package com.arishev.task.kafka;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClientConsumer {

    private final NotificationService notificationService;


    @KafkaListener(id = "task-listener",
                    topics = "${task-service.kafka.task-status-topic}",
                    containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskDTO> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {


        log.debug("[Kafka consumer]: Started handling messages from topic %s".formatted(topic));
        int messageIndex = 0;
        try {
            log.info("[Kafka consumer]: Message List = " + messageList);

            for (TaskDTO task : messageList) {
                notificationService.notify("Task with id = " + task.getId() +
                        " got a new status = " + task.getStatus().name());
                messageIndex++;
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error during handling messages from Kafka", e);
            ack.nack(messageIndex, Duration.ofSeconds(3));
        }

        log.debug("[Kafka consumer]: Ended handling messages from topic %s".formatted(topic));
    }
}

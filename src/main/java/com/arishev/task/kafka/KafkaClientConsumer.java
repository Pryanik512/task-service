package com.arishev.task.kafka;

import com.arishev.task.dto.TaskDTO;
import com.arishev.task.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaClientConsumer {

    private NotificationService notificationService;


    @KafkaListener(id = "task-listener",
                    topics = "${task-service.kafka.task-status-topic}",
                    containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskDTO> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {


        log.debug("Kafka consumer: message handling started");
        try {
            log.info("Message List = " + messageList);
            messageList
                    .forEach(task -> notificationService.notify("Task with id = " + task.getId() +
                                                                " got a new status = " + task.getStatus().name()));

        } catch (Exception e) {
            log.error("Error during handling message from Kafka", e);
        } finally {

            ack.acknowledge();
        }
        log.debug("Kafka consumer: message handling ended");
    }
}

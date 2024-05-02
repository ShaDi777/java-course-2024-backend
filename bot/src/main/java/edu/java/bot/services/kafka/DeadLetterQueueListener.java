package edu.java.bot.services.kafka;

import edu.java.bot.dto.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadLetterQueueListener {
    @KafkaListener(topics = "${app.dead-topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenBadUpdates(LinkUpdateRequest update) {
        log.info(update.toString());
    }
}

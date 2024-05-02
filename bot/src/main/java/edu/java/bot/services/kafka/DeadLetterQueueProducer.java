package edu.java.bot.services.kafka;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeadLetterQueueProducer {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void sendToDlq(LinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.deadTopic().name(), update);
    }
}

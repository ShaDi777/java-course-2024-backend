package edu.java.services.sender;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.BotLinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
@RequiredArgsConstructor
public class ScrapperQueueProducer implements UpdateSender {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, BotLinkUpdateRequest> kafkaTemplate;

    public void send(BotLinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.topic().name(), update);
    }
}

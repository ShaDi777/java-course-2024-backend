package edu.java.bot.services.kafka;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.services.LinkUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUpdateListener {
    private final LinkUpdateHandler updateHandler;
    private final DeadLetterQueueProducer dlqProducer;

    @KafkaListener(
        topics = "${app.topic.name}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listen(LinkUpdateRequest linkUpdateRequest) {
        try {
            updateHandler.handle(
                linkUpdateRequest.url().toString(),
                linkUpdateRequest.description(),
                linkUpdateRequest.tgChatsIds()
            );
        } catch (Exception e) {
            dlqProducer.sendToDlq(linkUpdateRequest);
        }
    }
}

package edu.java.bot.kafka;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.BotApplication;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.services.LinkUpdateHandler;
import edu.java.bot.services.kafka.DeadLetterQueueListener;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = {BotApplication.class})
@DirtiesContext
@EnableConfigurationProperties(ApplicationConfig.class)
public class KafkaUpdateListenerTest extends KafkaIntegrationTest {
    private static final LinkUpdateRequest REQUEST = new LinkUpdateRequest(1L, URI.create(""), "", new Long[] {});

    @Autowired
    private ApplicationConfig applicationConfig;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @MockBean
    private LinkUpdateHandler linkUpdateHandlerMock;

    @SpyBean
    private DeadLetterQueueListener deadLetterQueueListener;

    @MockBean
    private TelegramBot telegramBot;

    @Test
    void listenValidUpdateMessage() {
        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(getProducerProps())) {
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.topic().name(), REQUEST));

            Mockito.verify(linkUpdateHandlerMock, Mockito.after(3000).times(1))
                   .handle(REQUEST.url().toString(), REQUEST.description(), REQUEST.tgChatsIds());
        }
    }

    @Test
    void listenInvalidUpdateMessage() {
        Mockito.doThrow(RuntimeException.class)
               .when(linkUpdateHandlerMock)
               .handle(Mockito.any(), Mockito.any(), Mockito.any());

        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(getProducerProps())) {
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.topic().name(), REQUEST));

            Mockito.verify(linkUpdateHandlerMock, Mockito.after(3000).times(1))
                   .handle(REQUEST.url().toString(), REQUEST.description(), REQUEST.tgChatsIds());

            Mockito.verify(deadLetterQueueListener, Mockito.after(3000).times(1))
                   .listenBadUpdates(Mockito.any());
        }
    }

    private Map<String, Object> getProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}

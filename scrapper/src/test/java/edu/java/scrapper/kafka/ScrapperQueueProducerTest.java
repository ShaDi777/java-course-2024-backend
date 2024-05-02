package edu.java.scrapper.kafka;

import edu.java.ScrapperApplication;
import edu.java.configuration.ApplicationConfig;
import edu.java.dto.bot.BotLinkUpdateRequest;
import edu.java.services.sender.UpdateSender;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScrapperApplication.class, properties = "app.use-queue=true")
@DirtiesContext
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperQueueProducerTest extends KafkaIntegrationTest {
    private static final BotLinkUpdateRequest REQUEST = new BotLinkUpdateRequest(1L, URI.create(""), "", new Long[] {});
    @Autowired
    private ApplicationConfig applicationConfig;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private UpdateSender updateSender;

    @Test
    void produceValidMessage() {
        try (KafkaConsumer<String, BotLinkUpdateRequest> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())) {
            kafkaConsumer.subscribe(List.of(applicationConfig.topic().name()));

            updateSender.send(REQUEST);

            var records = kafkaConsumer.poll(Duration.ofSeconds(5))
                                       .records(applicationConfig.topic().name());
            assertThat(records).allMatch(message -> message.value().equals(REQUEST));
        }

    }

    private Map<String, Object> getConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }
}

package com.concertmania.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "concert-events" }, brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnableKafka
public class ConcertKafkaIntegrationTest {

    private static final String TOPIC = "concert-events";

    private EmbeddedKafkaBroker embeddedKafkaBroker;


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private KafkaMessageListenerContainer<String, Object> container;
    private BlockingQueue<ConsumerRecord<String, Object>> records;

    @BeforeAll
    void setup() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "false", embeddedKafkaBroker);
        consumerProps.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps,
                new org.apache.kafka.common.serialization.StringDeserializer(), new JsonDeserializer<>());

        ContainerProperties containerProps = new ContainerProperties(TOPIC);

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);

        records = new LinkedBlockingQueue<>();

        container.setupMessageListener((MessageListener<String, Object>) record -> {
            records.add(record);
        });

        container.start();
    }

    @AfterAll
    void tearDown() {
        if (container != null) {
            container.stop();
        }
    }

    @Test
    void testSendReceive() throws InterruptedException {
        String testMessage = "Hello Kafka Test!";

        kafkaTemplate.send(TOPIC, testMessage);

        ConsumerRecord<String, Object> received = records.poll(10, TimeUnit.SECONDS);
        assertThat(received).isNotNull();
        assertThat(received.value()).isEqualTo(testMessage);
    }
}

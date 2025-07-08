package com.concertmania.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"concert-events"})
public class ConnectKafkaTest {

    @Test
    void contextLoads() {
        // 컨텍스트가 정상 로드되는지 테스트
    }
}

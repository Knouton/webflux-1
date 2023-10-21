package com.demo.webflux.kafka;

import com.demo.webflux.dto.AuthEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
	@Value("${spring.kafka.producer.topic.load.auth.event}")
	private String authEventTopic;
	private final KafkaSender<String, String> kafkaSender;

	public void startAuthEventMessage(Mono<AuthEvent> authEvent) {
		String key = UUID.randomUUID().toString();
		kafkaSender.send(authEvent.map(
				i -> SenderRecord.create(new ProducerRecord<>(authEventTopic, key, i.getMessage()), key)))
				.doOnError(e -> log.error("Send failed", e))
				.subscribe(r -> {
					RecordMetadata metadata = r.recordMetadata();
					log.debug("Message {} sent successfully, topic-partition={}-{} offset={} timestamp={}",
					          r.correlationMetadata(),
					          metadata.topic(),
					          metadata.partition(),
					          metadata.offset(),
					          LocalDateTime.now()
					);
				});
	}
}

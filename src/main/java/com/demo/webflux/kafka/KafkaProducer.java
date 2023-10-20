package com.demo.webflux.kafka;

import com.demo.webflux.config.KafkaProducerConfig;
import com.demo.webflux.dto.AuthEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
	@Value("${spring.kafka.producer.topic.load.auth.event}")
	private String authEventTopic;
	private final KafkaProducerConfig producerConfig;
	public void startAuthEventMessage(AuthEvent authEvent) {
		producerConfig.getAuthEventEntryProducerTemplate().send(authEventTopic, authEvent);
	}
}

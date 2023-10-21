package com.demo.webflux.service.adapter;

import com.demo.webflux.dto.AuthEvent;
import com.demo.webflux.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthEventService {
	private final KafkaProducer kafkaProducer;
    public void sendAuthEvent(Mono<AuthEvent> authEvent) {
		kafkaProducer.startAuthEventMessage(authEvent);
    }
}

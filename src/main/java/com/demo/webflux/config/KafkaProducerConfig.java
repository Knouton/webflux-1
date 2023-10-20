package com.demo.webflux.config;

import com.demo.webflux.dto.AuthEvent;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaProducerConfig {

	private final KafkaCommonConfig commonConfig;

	@Bean
	public Map<String, Object> producerConfigs() {
		return commonConfig.producerConfig();
	}

	@Bean
	public ProducerFactory<Long, AuthEvent> producerAuthEventFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<Long, AuthEvent> getAuthEventEntryProducerTemplate() {
		KafkaTemplate<Long, AuthEvent> template = new KafkaTemplate<>(producerAuthEventFactory());
		template.setMessageConverter(new StringJsonMessageConverter());
		return template;
	}
}

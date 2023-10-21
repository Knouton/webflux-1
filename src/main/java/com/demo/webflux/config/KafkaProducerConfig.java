package com.demo.webflux.config;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
	private final KafkaCommonConfig commonConfig;
	@Bean
	public KafkaSender<String, String> getKafkaSender() {
		Map<String, Object> props = commonConfig.getProducerConfig();
		SenderOptions<String, String> senderOptions = SenderOptions.create(props);

		return KafkaSender.create(senderOptions);
	}
}

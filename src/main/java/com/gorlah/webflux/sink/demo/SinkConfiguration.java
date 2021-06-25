package com.gorlah.webflux.sink.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Configuration
class SinkConfiguration {

    @Bean
    Sinks.Many<UUID> uuidSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> printMostSignificantBits(Sinks.Many<UUID> uuidSink) {
        return event -> uuidSink.asFlux()
                .delayElements(Duration.ofSeconds(1))
                .map(UUID::getMostSignificantBits)
                .doOnNext(it -> log.info("Most significant bits: " + it.toString()))
                .subscribe();
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> printLeastSignificantBits(Sinks.Many<UUID> uuidSink) {
        return event -> uuidSink.asFlux()
                .delayElements(Duration.ofSeconds(2))
                .map(UUID::getLeastSignificantBits)
                .doOnNext(it -> log.info("Least significant bits: " + it.toString()))
                .subscribe();
    }
}

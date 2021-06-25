package com.gorlah.webflux.sink.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
class UUIDController {

    private final Sinks.Many<UUID> uuidSink;

    @GetMapping("/uuids")
    Mono<UUID> getUUID() {
        return Mono.just(UUID.randomUUID())
                .doOnSuccess(it -> log.info(it.toString()))
                .doOnSuccess(uuidSink::tryEmitNext);
    }
}

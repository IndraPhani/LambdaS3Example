package com.example;

import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class InventoryHandler {
@Builder
    class BeerInventoryDto{
        public UUID id;
    public UUID beerId;
    public Long quantityOnHand;
    public OffsetDateTime createdDate;
    public OffsetDateTime lastModifiedDate;
    }
@Bean
    public Mono<ServerResponse> listInventory(ServerRequest request){
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(Mono.just(Arrays.asList(
                            BeerInventoryDto.builder()
                                    .id(UUID.randomUUID())
                                    .beerId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                                    .quantityOnHand(999L)
                                    .createdDate(OffsetDateTime.now())
                                    .lastModifiedDate(OffsetDateTime.now())
                                    .build())), List.class);
        }
    }
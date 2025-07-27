package com.nyanmyohtet.springbootstarter.client;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

public class ApiErrorHandler {

    public static ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                return response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException(
                                "API error: " + response.statusCode() + " - " + body)));
            }
            return Mono.just(response);
        });
    }
}

package com.nyanmyohtet.springbootstarter.client;

import com.nyanmyohtet.springbootstarter.client.dto.TokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient webClient;

    public AuthClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://account-d.docusign.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    public Mono<String> requestAccessToken(String jwtAssertion) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
                        .queryParam("assertion", jwtAssertion)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class);
    }
}

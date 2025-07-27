package com.nyanmyohtet.springbootstarter.client;

import com.nyanmyohtet.springbootstarter.client.model.ApiMethod;
import com.nyanmyohtet.springbootstarter.client.model.QueryParamsBuilder;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiClient {

    private WebClient webClient;

    private final String BEARER = "Bearer ";

    public <T, R> Mono<R> callApi(
            String path,
            ApiMethod method,
            String jwtToken,
            @Nullable T requestBody,
            Class<R> responseType,
            Map<String, String> customHeaders,
            Map<String, String> queryParams
    ) {
        WebClient.RequestBodySpec requestSpec = webClient
                .method(convertToHttpMethod(method))
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (queryParams != null && !queryParams.isEmpty()) {
                        MultiValueMap<String, String> params = QueryParamsBuilder.build(queryParams);
                        uriBuilder.queryParams(params);
                    }
                    return uriBuilder.build();
                })
                .header(HttpHeaders.AUTHORIZATION, BEARER + jwtToken)
                .headers(httpHeaders -> {
                    if (customHeaders != null) {
                        customHeaders.forEach(httpHeaders::set);
                    }
                })
                .accept(MediaType.APPLICATION_JSON);

        if (method == ApiMethod.POST || method == ApiMethod.PUT) {
            requestSpec = (WebClient.RequestBodySpec) requestSpec
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody);
        }
        return requestSpec
                .retrieve()
                .bodyToMono(responseType)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)));
        // uses exponential backoff: waits longer after each failure (e.g. 1s → 2s → 4s)
        // .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
    }

    private HttpMethod convertToHttpMethod(ApiMethod method) {
        return switch (method) {
            case GET -> HttpMethod.GET;
            case POST -> HttpMethod.POST;
            case PUT -> HttpMethod.PUT;
            case DELETE -> HttpMethod.DELETE;
        };
    }
}

package com.nyanmyohtet.springbootstarter.client.model;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class QueryParamsBuilder {

    public static MultiValueMap<String, String> build(Map<String, String> params) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        params.forEach(queryParams::add);
        return queryParams;
    }
}

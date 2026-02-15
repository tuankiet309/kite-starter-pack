package com.kite.http.client.impl;

import com.kite.http.client.KiteHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * RestTemplate-based implementation of KiteHttpClient
 */
@RequiredArgsConstructor
public class RestTemplateHttpClient implements KiteHttpClient {

    private final RestTemplate restTemplate;

    @Override
    public <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> T get(String url, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders(headers);
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return response.getBody();
    }

    @Override
    public <T> T post(String url, Object body, Class<T> responseType) {
        return restTemplate.postForObject(url, body, responseType);
    }

    @Override
    public <T> T post(String url, Object body, Map<String, String> headers, Class<T> responseType) {
        HttpHeaders httpHeaders = createHeaders(headers);
        HttpEntity<?> entity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        return response.getBody();
    }

    @Override
    public <T> T put(String url, Object body, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(body);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
        return response.getBody();
    }

    @Override
    public <T> T delete(String url, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, null, responseType);
        return response.getBody();
    }

    @Override
    public <T> T patch(String url, Object body, Class<T> responseType) {
        HttpEntity<?> entity = new HttpEntity<>(body);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, responseType);
        return response.getBody();
    }

    private HttpHeaders createHeaders(Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        return httpHeaders;
    }
}

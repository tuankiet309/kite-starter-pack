package com.kite.http.client;

import java.util.Map;

/**
 * Unified HTTP client abstraction for making external API calls.
 * Supports multiple underlying implementations (RestTemplate, WebClient, OkHttp, Apache HttpClient).
 */
public interface KiteHttpClient {

    /**
     * Perform a GET request
     * @param url Target URL
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T get(String url, Class<T> responseType);

    /**
     * Perform a GET request with headers
     * @param url Target URL
     * @param headers Request headers
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T get(String url, Map<String, String> headers, Class<T> responseType);

    /**
     * Perform a POST request
     * @param url Target URL
     * @param body Request body
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T post(String url, Object body, Class<T> responseType);

    /**
     * Perform a POST request with headers
     * @param url Target URL
     * @param body Request body
     * @param headers Request headers
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T post(String url, Object body, Map<String, String> headers, Class<T> responseType);

    /**
     * Perform a PUT request
     * @param url Target URL
     * @param body Request body
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T put(String url, Object body, Class<T> responseType);

    /**
     * Perform a DELETE request
     * @param url Target URL
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T delete(String url, Class<T> responseType);

    /**
     * Perform a PATCH request
     * @param url Target URL
     * @param body Request body
     * @param responseType Expected response class
     * @return Response object
     */
    <T> T patch(String url, Object body, Class<T> responseType);
}

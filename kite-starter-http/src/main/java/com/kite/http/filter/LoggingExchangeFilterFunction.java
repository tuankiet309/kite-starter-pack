package com.kite.http.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
public class LoggingExchangeFilterFunction implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        if (log.isDebugEnabled()) {
            log.debug("===========================request begin================================================");
            log.debug("URI         : {}", request.url());
            log.debug("Method      : {}", request.method());
            log.debug("Headers     : {}", request.headers());
            log.debug("==========================request end================================================");
        }
        return next.exchange(request)
                .doOnNext(response -> {
                    if (log.isDebugEnabled()) {
                        log.debug("============================response begin==========================================");
                        log.debug("Status code  : {}", response.statusCode());
                        log.debug("Headers      : {}", response.headers().asHttpHeaders());
                        log.debug("=======================response end=================================================");
                    }
                });
    }
}

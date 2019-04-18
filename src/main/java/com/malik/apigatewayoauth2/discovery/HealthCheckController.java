package com.malik.apigatewayoauth2.discovery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthCheckController {

    @RequestMapping(method = RequestMethod.GET, path = "/health-check")
    public Mono<ResponseEntity<String>> getHealth() {
        return Mono.just(ResponseEntity.ok("healthCheck"));
    }
}

package com.malik.apigatewayoauth2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest
//@SpringBootTest
public class ApiGatewayOauth2ApplicationTests {

    @Autowired
    WebTestClient webClient;

    @Test
    public void contextLoads() {

    }

    @Test
    public void testHealthCheck() {

        webClient.get()
                .uri("/health-check")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("healthCheck");
    }

}

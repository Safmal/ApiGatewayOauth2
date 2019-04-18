package com.malik.apigatewayoauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;


@EnableWebFluxSecurity
@Import({SecurityProperties.class})
public class SecurityConfig {

    private SecurityProperties securityProperties;

    public SecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeExchange()//.pathMatchers(securityProperties.getApiMatcher())
                .anyExchange()
                .authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(reactiveGrantedAuthoritiesExtractor());
        ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (null != securityProperties.getCorsConfiguration()) {
            source.registerCorsConfiguration("/**", securityProperties.getCorsConfiguration());
        }
        return source;
    }

    //Because keycloak doesn't hold client roles in default manner, we need to get their
    @Bean
    public JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        return new GrantedAuthoritiesExtractor();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter reactiveGrantedAuthoritiesExtractor() {
        return new ReactiveJwtAuthenticationConverterAdapter(grantedAuthoritiesExtractor());
    }
}

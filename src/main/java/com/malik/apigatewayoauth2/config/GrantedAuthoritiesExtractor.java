package com.malik.apigatewayoauth2.config;

import net.minidev.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class GrantedAuthoritiesExtractor extends JwtAuthenticationConverter {


    @Override
    protected Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        Map<String, ?> claims = jwt.getClaims();

        String clientId = (String)claims.get("azp");

        JSONObject resourceAccess = (JSONObject)claims.get("resource_access");
        JSONObject client = (JSONObject)resourceAccess.get(clientId);
        Collection<String> roles = (Collection<String>)client.get("roles");
        return roles.stream()
                .map(r -> "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}

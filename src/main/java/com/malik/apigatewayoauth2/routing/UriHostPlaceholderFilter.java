package com.malik.apigatewayoauth2.routing;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Component
public class UriHostPlaceholderFilter extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
            Map<String, String> uriVariables = exchange.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if ((uriVariables == null) || (route == null)) {
                return chain.filter(exchange);
            }
            URI uri = route.getUri();
            String[] hostWithVersion = uri.getHost().split("-");
            String host = hostWithVersion[0];
            String version = hostWithVersion[1];
            if ((host != null && version != null) && (uriVariables.containsKey(host) && uriVariables.containsKey(version))) {
                host = uriVariables.get(host) + "-" + uriVariables.get(version);
            }
            if (host == null) {
                return chain.filter(exchange);
            }
            URI newUri = UriComponentsBuilder.fromUri(uri).host(host).build().toUri();
            Route newRoute = Route.async() // use async builder instead of basic builder because or return value of "route.getPredicate()" which is AsyncPredicate
                    .id(route.getId())
                    .uri(newUri)
                    .order(route.getOrder())
                    .filters(route.getFilters())
                    .asyncPredicate(route.getPredicate())
                    .build();
            exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, newRoute);
            return chain.filter(exchange);
        };
    }
}

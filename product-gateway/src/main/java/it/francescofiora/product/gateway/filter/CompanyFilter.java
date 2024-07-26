package it.francescofiora.product.gateway.filter;

import it.francescofiora.product.gateway.config.UriConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filter for Company Api Auth.
 */
@RefreshScope
@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyFilter implements GatewayFilter {

  private final UriConfiguration uriConfiguration;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    var user = uriConfiguration.getCompany().getUserName();
    var password = uriConfiguration.getCompany().getPassword();
    var request = exchange
        .getRequest()
        .mutate()
        .headers(headers -> headers.setBasicAuth(user, password))
        .build();
    log.debug("CompanyFilter: {} {}", request.getMethod(), request.getPath());
    var exchangeMuted = exchange.mutate().request(request).build();
    return chain.filter(exchangeMuted);
  }
}

package it.francescofiora.product.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

/**
 * Security Configuration.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  /**
   * Configure Basic authentication.
   *
   * @param http HttpSecurity
   * @return SecurityWebFilterChain
   */
  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange(authorize -> authorize
            .matchers(ServerWebExchangeMatchers.pathMatchers("/actuator/**"))
            .permitAll()
            .anyExchange().authenticated())
        .httpBasic(Customizer.withDefaults())
        .csrf(CsrfSpec::disable);
    return http.build();
  }
}

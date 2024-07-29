package it.francescofiora.product.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
        .httpBasic(Customizer.withDefaults())
        .authorizeExchange(authorize -> authorize.anyExchange().authenticated())
        .csrf(CsrfSpec::disable);
    return http.build();
  }
}

package it.francescofiora.product.company.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration.
 */
@Configuration
public class SecurityConfig {

  /**
   * Configure Basic authentication.
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception if errors occur
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers("/actuator/**")
                .permitAll()
                .anyRequest()
                .authenticated())
        .httpBasic(Customizer.withDefaults());
    // @formatter:on
    return http.build();
  }
}

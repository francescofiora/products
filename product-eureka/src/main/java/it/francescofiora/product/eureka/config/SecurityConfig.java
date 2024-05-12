package it.francescofiora.product.eureka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**"));
    return http. build();
  }
}

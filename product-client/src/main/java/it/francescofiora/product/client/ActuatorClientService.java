package it.francescofiora.product.client;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * Actuator Client Service.
 */
public interface ActuatorClientService {

  /**
   * Get the info.
   *
   * @return the response.
   */
  Mono<ResponseEntity<String>> getInfo();

  /**
   * Get the health.
   *
   * @return the response.
   */
  Mono<ResponseEntity<String>> getHealth();
}

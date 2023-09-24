package it.francescofiora.product.client.impl;

import it.francescofiora.product.client.ActuatorClientService;
import it.francescofiora.product.client.ClientInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Actuator Client ServiceImpl.
 */
@Component
public class ActuatorClientServiceImpl extends AbtractClient implements ActuatorClientService {

  /**
   * Constructor.
   *
   * @param clientInfo the ClientInfo
   */
  public ActuatorClientServiceImpl(ClientInfo clientInfo) {
    super(clientInfo);
  }

  @Override
  public Mono<ResponseEntity<String>> getInfo() {
    return get("/actuator/info", String.class);
  }

  @Override
  public Mono<ResponseEntity<String>> getHealth() {
    return get("/actuator/health", String.class);
  }
}

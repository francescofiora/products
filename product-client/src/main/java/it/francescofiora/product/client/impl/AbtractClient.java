package it.francescofiora.product.client.impl;

import it.francescofiora.product.api.service.dto.DtoIdentifier;
import it.francescofiora.product.client.ClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Abtract Http Client.
 */
@RequiredArgsConstructor
public abstract class AbtractClient {

  private final ClientInfo clientInfo;

  protected WebClient buildWebClient() {
    // @formatter:off
    return WebClient.builder()
        .baseUrl(clientInfo.getBaseUrl())
        .defaultHeaders(header -> {
          header.setBasicAuth(clientInfo.getUserName(), clientInfo.getPassword());
          header.setContentType(MediaType.APPLICATION_JSON);
        }).build();
    // @formatter:on
  }

  protected <T> Mono<ResponseEntity<Void>> create(String uri, T body, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .post()
        .uri(uri)
        .body(Mono.just(body), clazz)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }

  protected <T extends DtoIdentifier> Mono<ResponseEntity<Void>> update(String uri, T body,
      Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .put()
        .uri(uri, body.getId())
        .body(Mono.just(body), clazz)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }

  protected <T> Flux<T> findAll(Pageable pageable, String uri, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(clazz);
    // @formatter:on
  }

  protected <T> Mono<ResponseEntity<T>> get(String url, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(url)
        .retrieve()
        .toEntity(clazz);
    // @formatter:on
  }

  protected <T> Mono<ResponseEntity<T>> findOne(String url, Long id, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(url, id)
        .retrieve()
        .toEntity(clazz);
    // @formatter:on
  }

  protected Mono<ResponseEntity<Void>> delete(String url, Long id) {
    // @formatter:off
    return buildWebClient()
        .delete()
        .uri(url, id)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }
}

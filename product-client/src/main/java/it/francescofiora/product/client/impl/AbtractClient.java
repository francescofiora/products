package it.francescofiora.product.client.impl;

import it.francescofiora.product.client.ClientInfo;
import it.francescofiora.product.common.dto.DtoIdentifier;
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

  /**
   * Build WebClient.
   *
   * @return the WebClient
   */
  protected WebClient buildWebClient() {
    // @formatter:off
    var builder = WebClient.builder()
        .baseUrl(clientInfo.getBaseUrl())
        .defaultHeaders(header -> {
          header.setBasicAuth(clientInfo.getUserName(), clientInfo.getPassword());
          header.setContentType(MediaType.APPLICATION_JSON);
        });
    // @formatter:on
    return builder.build();
  }

  /**
   * Call POST endpoint to create a new resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param body the body
   * @param clazz the class of the body
   * @return the ResponseEntity
   */
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

  /**
   * Call POST endpoint with id to create a new resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param id the id
   * @param body the body
   * @param clazz the class of the body
   * @return the ResponseEntity
   */
  protected <T> Mono<ResponseEntity<Void>> create(String uri, Long id, T body, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .post()
        .uri(uri, id)
        .body(Mono.just(body), clazz)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }

  /**
   * Call PUT endpoint to update a resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param body the body
   * @param clazz the class of the body
   * @return the ResponseEntity
   */
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

  /**
   * Call PUT endpoint to patch a resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param body the body
   * @param clazz the class of the body
   * @return the ResponseEntity
   */
  protected <T extends DtoIdentifier> Mono<ResponseEntity<Void>> patch(String uri, T body,
      Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .patch()
        .uri(uri, body.getId())
        .body(Mono.just(body), clazz)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }

  /**
   * Call GET endpoint to return resources in pages.
   *
   * @param <T> Class type of the body
   * @param pageable the Pageable
   * @param uri the uri
   * @param clazz the class of the result
   * @return the resources in pages
   */
  protected <T> Flux<T> findAll(Pageable pageable, String uri, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(uri)
        .retrieve()
        .bodyToFlux(clazz);
    // @formatter:on
  }

  /**
   * Call GET endpoint to return a resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param clazz the class of the result
   * @return the ResponseEntity
   */
  protected <T> Mono<ResponseEntity<T>> get(String uri, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(uri)
        .retrieve()
        .toEntity(clazz);
    // @formatter:on
  }

  /**
   * Call GET endpoint to return a resource.
   *
   * @param <T> Class type of the body
   * @param uri the uri
   * @param id the id of the resource
   * @param clazz the class of the result
   * @return the ResponseEntity
   */
  protected <T> Mono<ResponseEntity<T>> findOne(String uri, Long id, Class<T> clazz) {
    // @formatter:off
    return buildWebClient()
        .get()
        .uri(uri, id)
        .retrieve()
        .toEntity(clazz);
    // @formatter:on
  }

  /**
   * Call DELETE endpoint to delete a resource.
   *
   * @param uri the uri
   * @param ids list of id
   * @return a void ResponseEntity
   */
  protected Mono<ResponseEntity<Void>> delete(String uri, Object... ids) {
    // @formatter:off
    return buildWebClient()
        .delete()
        .uri(uri, ids)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }
}

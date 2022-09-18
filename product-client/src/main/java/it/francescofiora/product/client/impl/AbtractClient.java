package it.francescofiora.product.client.impl;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import it.francescofiora.product.api.service.dto.DtoIdentifier;
import it.francescofiora.product.client.ClientInfo;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * Abtract Http Client.
 */
@RequiredArgsConstructor
public abstract class AbtractClient {

  private final ClientInfo clientInfo;

  protected SslContext createSslContext() {
    try {
      var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(getClass().getClassLoader().getResourceAsStream(clientInfo.getKeyStoreFile()),
          clientInfo.getKeyStorePassword().toCharArray());

      var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keyStore, clientInfo.getKeyStorePassword().toCharArray());

      return SslContextBuilder.forClient().keyManager(keyManagerFactory).build();

    } catch (Exception e) {
      throw new RuntimeException("Error creating SSL context.");
    }
  }

  protected WebClient buildWebClient() {
    // @formatter:off
    var builder = WebClient.builder()
        .baseUrl(clientInfo.getBaseUrl())
        .defaultHeaders(header -> {
          header.setBasicAuth(clientInfo.getUserName(), clientInfo.getPassword());
          header.setContentType(MediaType.APPLICATION_JSON);
        });
    // @formatter:on
    if (clientInfo.isSslEnabled()) {
      var httpClient = HttpClient.create().secure(t -> t.sslContext(createSslContext()));
      builder.clientConnector(new ReactorClientHttpConnector(httpClient));
    }
    return builder.build();
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

  protected Mono<ResponseEntity<Void>> delete(String url, Object... ids) {
    // @formatter:off
    return buildWebClient()
        .delete()
        .uri(url, ids)
        .retrieve()
        .toEntity(Void.class);
    // @formatter:on
  }
}

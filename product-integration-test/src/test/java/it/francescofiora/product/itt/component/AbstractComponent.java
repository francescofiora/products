package it.francescofiora.product.itt.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract Component.
 */
public class AbstractComponent {

  protected Long validateResponseAndGetId(ResponseEntity<Void> result) {
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertNotNull(result.getHeaders().get(HttpHeaders.LOCATION));
    var locations = result.getHeaders().get(HttpHeaders.LOCATION);
    assertThat(locations).isNotNull().isNotEmpty();
    var url = locations.get(0);
    assertNotNull(url);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }
}

package it.francescofiora.product.api.web.api.impl;

import it.francescofiora.product.api.web.errors.NotFoundAlertException;
import it.francescofiora.product.api.web.util.HeaderUtil;
import it.francescofiora.product.api.web.util.PaginationUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

/**
 * Abstract Api RestController.
 */
public abstract class AbstractApi {

  private final String entityName;

  protected AbstractApi(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Create a ResponseEntity of a POST action.
   *
   * @param path the path
   * @param id the id of the resource created
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> postResponse(final String path, final Long id) {
    return postResponse(entityName, path, id);
  }

  /**
   * Create a ResponseEntity of a POST action.
   *
   * @param refEntityName the entityName
   * @param path the path
   * @param id the id of the resource created
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> postResponse(final String refEntityName, final String path,
      final Long id) {
    // @formatter:off
    return ResponseEntity
        .created(createUri(path))
        .headers(HeaderUtil.createAlert(refEntityName + HeaderUtil.CREATED, String.valueOf(id)))
        .build();
    // @formatter:on
  }

  /**
   * Create URI object from string.
   *
   * @param uri the URI in String to be converted
   * @return the URI created
   */
  public static URI createUri(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }
  }

  /**
   * Create a ResponseEntity of a PUT action.
   *
   * @param id the id of the resource updated
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> putResponse(final Long id) {
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createAlert(entityName + HeaderUtil.UPDATED, String.valueOf(id)))
        .build();
    // @formatter:on
  }


  /**
   * Create a ResponseEntity of a PATCH action.
   *
   * @param id the id of the resource patched
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> patchResponse(final Long id) {
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(HeaderUtil.createAlert(entityName + HeaderUtil.PATCHED, String.valueOf(id)))
        .build();
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final Page<T> page) {
    return getResponse(entityName, page);
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param refEntityName the entity Name
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final String refEntityName,
      final Page<T> page) {
    var headers = PaginationUtil.getHttpHeadersfromPagination(refEntityName, page);
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(headers)
        .body(page.getContent());
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a GET action with an OK status, or if it's empty, it returns a
   * ResponseEntity with NOT_FOUND.
   *
   * @param <T> type of the response
   * @param maybeResponse response to return if present
   * @return response containing {@code maybeResponse} if present or
   *         {@link org.springframework.http.HttpStatus#NOT_FOUND}
   */
  protected <T> ResponseEntity<T> getResponse(Optional<T> maybeResponse, Long id) {
    // @formatter:off
    return maybeResponse
        .map(response -> ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(entityName + HeaderUtil.GET, String.valueOf(id)))
            .body(response))
        .orElseThrow(() -> new NotFoundAlertException(entityName, String.valueOf(id),
            entityName + " not found with id " + id));
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a DELETE action.
   *
   * @param id the id of the resource deleted
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> deleteResponse(final Long id) {
    return deleteResponse(entityName, id);
  }

  /**
   * Create a ResponseEntity of a DELETE action.
   *
   * @param refEntityName the entity Name
   * @param id the id of the resource deleted
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> deleteResponse(final String refEntityName, final Long id) {
    // @formatter:off
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createAlert(refEntityName + HeaderUtil.DELETED, String.valueOf(id)))
        .build();
    // @formatter:on
  }
}

package it.francescofiora.product.company.api.web.util;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * Header Util.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderUtil {

  public static final String X_ALERT = "X-alert";
  public static final String X_ERROR = "X-error";
  public static final String X_PARAMS = "X-params";

  /**
   * Create Entity Creation Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(entityName + ".created", param);
  }

  /**
   * Create Entity Get Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createEntityGetAlert(String entityName, String param) {
    return createAlert(entityName + ".get", param);
  }

  /**
   * Create Entity Update Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
    return createAlert(entityName + ".updated", param);
  }

  /**
   * Create Entity Patch Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createEntityPatchedAlert(String entityName, String param) {
    return createAlert(entityName + ".patched", param);
  }

  /**
   * Create Entity Deletion Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(entityName + ".deleted", param);
  }

  /**
   * Create Failure Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  public static HttpHeaders createFailureAlert(String alert, String param, String errorMessage) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.add(X_PARAMS, param);
    headers.add(X_ERROR, errorMessage);
    return headers;
  }

  /**
   * Create Failure Alert.
   *
   * @param alert the message
   * @param params the list of parameters
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  public static HttpHeaders createFailureAlert(String alert, List<String> params,
      String errorMessage) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.addAll(X_PARAMS, params);
    headers.add(X_ERROR, errorMessage);
    return headers;
  }

  /**
   * Create Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @return HttpHeaders
   */
  public static HttpHeaders createAlert(String alert, String param) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.add(X_PARAMS, param);
    return headers;
  }
}

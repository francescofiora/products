package it.francescofiora.product.client;

/**
 * Client Properties.
 */
public interface ClientInfo {

  /**
   * Get base url.
   *
   * @return the base url
   */
  String getBaseUrl();

  /**
   * Get UserName.
   *
   * @return the UserName
   */
  String getUserName();

  /**
   * Get Password.
   *
   * @return the Password
   */
  String getPassword();
}

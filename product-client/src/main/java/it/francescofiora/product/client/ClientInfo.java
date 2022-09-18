package it.francescofiora.product.client;

/**
 * Client Properties.
 */
public interface ClientInfo {

  String getBaseUrl();

  String getUserName();

  String getPassword();

  String getKeyStoreFile();

  String getKeyStorePassword();

  boolean isSslEnabled();
}

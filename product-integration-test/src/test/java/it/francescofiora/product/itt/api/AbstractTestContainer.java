package it.francescofiora.product.itt.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Abstract TestContainer.
 */
public class AbstractTestContainer {

  private static Network network = Network.newNetwork();

  protected static PostgreSQLContainer<?> createPostgreSqlContainer() {
    // @formatter:off
    var mysql = new PostgreSQLContainer<>("postgres:14.1")
        .withNetwork(network)
        .withNetworkAliases("postgre")
        .withUsername("product").withPassword("secret")
        .withDatabaseName("db_product");
    // @formatter:on
    return mysql;
  }

  protected static GenericContainer<?> createContainer(String dockerImageName) {
    return new GenericContainer<>(dockerImageName).withNetwork(network);
  }

  protected static HikariDataSource createHikariDataSource(PostgreSQLContainer<?> container) {
    var hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(container.getDriverClassName());
    hikariConfig.setJdbcUrl(container.getJdbcUrl());
    hikariConfig.setUsername(container.getUsername());
    hikariConfig.setPassword(container.getPassword());

    return new HikariDataSource(hikariConfig);
  }

  protected static void executeQuery(HikariDataSource ds, String sql) throws SQLException {
    var statement = ds.getConnection().createStatement();
    statement.execute(sql);
  }

  protected static ResultSet performQuery(HikariDataSource ds, String sql) throws SQLException {
    var statement = ds.getConnection().createStatement();
    statement.execute(sql);
    var resultSet = statement.getResultSet();
    resultSet.next();
    return resultSet;
  }
}

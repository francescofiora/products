package it.francescofiora.product.itt.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * DataSource Utils.
 */
public class DataSourceUtils {

  /**
   * Create DataSource.
   *
   * @param container the PostgreSQL Container
   * @return the DataSource
   */
  public static HikariDataSource createHikariDataSource(PostgreSQLContainer<?> container) {
    var hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(container.getDriverClassName());
    hikariConfig.setJdbcUrl(container.getJdbcUrl());
    hikariConfig.setUsername(container.getUsername());
    hikariConfig.setPassword(container.getPassword());

    return new HikariDataSource(hikariConfig);
  }

  /**
   * Execute SQL Query.
   *
   * @param ds the DataSource
   * @param sql the SQL
   * @throws SQLException if errors occur
   */
  public static void executeQuery(HikariDataSource ds, String sql) throws SQLException {
    try (var statement = ds.getConnection().createStatement()) {
      statement.execute(sql);
    }
  }

  /**
   * Perform SQL Query and return the result.
   *
   * @param ds the DataSource
   * @param sql the SQL
   * @return the ResultSet
   * @throws SQLException if errors occur
   */
  public static ResultSet performQuery(HikariDataSource ds, String sql) throws SQLException {
    try (var statement = ds.getConnection().createStatement()) {
      statement.execute(sql);
      var resultSet = statement.getResultSet();
      resultSet.next();
      return resultSet;
    }
  }
}

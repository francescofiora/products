package it.francescofiora.product.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Abstract TestContainer.
 */
public class AbstractTestContainer {

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

  protected Long validateResponseAndGetId(ResponseEntity<Void> result) {
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertNotNull(result.getHeaders().get(HttpHeaders.LOCATION));
    var url = result.getHeaders().get(HttpHeaders.LOCATION).get(0);
    assertNotNull(url);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

}

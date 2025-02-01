package com.github.ankowals.example.rest.framework.environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresDb {

  private final Connection connection;
  private final PostgreSQLContainer<?> container;

  private PostgresDb(DockerImageName dockerImageName) throws SQLException {
    this.container = createContainer(dockerImageName);
    this.container.start();
    this.connection =
        DriverManager.getConnection(
            this.container.getJdbcUrl(),
            this.container.getUsername(),
            this.container.getPassword());
  }

  public static PostgresDb start() {
    try {
      return new PostgresDb(DockerImageName.parse("postgres:14.1"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Connection getConnection() {
    return this.connection;
  }

  public PostgreSQLContainer<?> getContainer() {
    return this.container;
  }

  private PostgreSQLContainer<?> createContainer(DockerImageName dockerImageName) {
    return new PostgreSQLContainer<>(dockerImageName)
        .withDatabaseName("postgres")
        .withUsername("postgres")
        .withPassword("postgres")
        .withExposedPorts(5432)
        .withReuse(true);
  }
}

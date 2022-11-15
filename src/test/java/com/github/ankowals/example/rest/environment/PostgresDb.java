package com.github.ankowals.example.rest.environment;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDb {

    private final Connection connection;

    private PostgresDb(DockerImageName dockerImageName) throws SQLException {
        this.connection = createConnection(startContainer(dockerImageName));
    }

    public static PostgresDb start() {
        try {
            return new PostgresDb(DockerImageName.parse("postgres:14.1"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection()  {
        return connection;
    }

    private Connection createConnection(PostgreSQLContainer<?> container) throws SQLException {
        return DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    private PostgreSQLContainer<?> startContainer(DockerImageName dockerImageName) {
        try(PostgreSQLContainer<?> container = new PostgreSQLContainer<>(dockerImageName)
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432)
                .withReuse(true)) {

            container.start();

            return container;
        }
    }
}

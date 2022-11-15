package com.github.ankowals.example.rest.environment;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDb {

    private final PostgreSQLContainer<?> container;

    private PostgresDb(PostgreSQLContainer<?> container) {
        this.container = container;
        container.start();
    }

    public static PostgresDb start() {
        return new PostgresDb(createContainer(DockerImageName.parse("postgres:14.1")));
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    private static PostgreSQLContainer<?> createContainer(DockerImageName dockerImageName) {
        return new PostgreSQLContainer<>(dockerImageName)
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432)
                .withReuse(true);
    }
}

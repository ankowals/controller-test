package com.github.ankowals.example.rest.environment;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDb {

    private final Connection connection;
    private final PostgreSQLContainer<?> container;

    private PostgresDb(DockerImageName dockerImageName) throws SQLException {
        this.container = createContainer(dockerImageName);
        container.start();

        this.connection = DriverManager.getConnection(container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword());
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

    public PostgreSQLContainer<?> getContainer() {
        return container;
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

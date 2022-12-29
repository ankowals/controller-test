package com.github.ankowals.example.rest.environment;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public interface StartsPostgres {

    PostgresDb POSTGRES_DB = PostgresDb.start();

    default Connection getPostgresConnection() { return POSTGRES_DB.getConnection(); }

    default Map<String, String> getPostgresProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("datasources.default.url", POSTGRES_DB.getContainer().getJdbcUrl());
        properties.put("datasources.default.username", POSTGRES_DB.getContainer().getUsername());
        properties.put("datasources.default.password", POSTGRES_DB.getContainer().getPassword());
        properties.put("datasources.default.driverClassName", POSTGRES_DB.getContainer().getDriverClassName());

        return properties;
    }
}

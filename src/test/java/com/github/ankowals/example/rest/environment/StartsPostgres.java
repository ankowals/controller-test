package com.github.ankowals.example.rest.environment;

import java.sql.Connection;

public interface StartsPostgres {

    PostgresDb POSTGRES_DB = PostgresDb.start();

    default Connection getPostgresConnection() { return POSTGRES_DB.getConnection(); }
}

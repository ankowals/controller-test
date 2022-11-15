package com.github.ankowals.example.rest.environment;

import java.sql.Connection;
import java.sql.SQLException;

public interface StartsPostgres {

    PostgresDb POSTGRES_DB = PostgresDb.start();

    default Connection getPostgresConnection() throws SQLException { return POSTGRES_DB.getConnection(); }
}

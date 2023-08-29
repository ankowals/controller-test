package com.github.ankowals.example.rest.framework.db.commands;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcConnectionQuery<T> {
    T run(Connection connection) throws SQLException, IOException;
}

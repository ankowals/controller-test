package com.github.ankowals.example.rest.framework.db.commands;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface JdbcConnectionCommand {
  void run(Connection connection) throws SQLException, IOException;
}

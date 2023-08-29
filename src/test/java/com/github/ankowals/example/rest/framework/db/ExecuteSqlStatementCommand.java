package com.github.ankowals.example.rest.framework.db;

import com.github.ankowals.example.rest.framework.db.commands.JdbcConnectionCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecuteSqlStatementCommand implements JdbcConnectionCommand {

    private final String statement;

    ExecuteSqlStatementCommand(String statement) {
        this.statement = statement;
    }

    @Override
    public void run(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(this.statement)) {
            ps.executeUpdate();
        }

        if (!connection.getAutoCommit())
            connection.commit();
    }
}

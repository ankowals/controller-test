package com.github.ankowals.example.rest.framework.db;

import com.github.ankowals.example.rest.framework.db.commands.JdbcConnectionCommand;
import com.github.ankowals.example.rest.framework.loaders.ResourceLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ExecuteSqlScriptCommand implements JdbcConnectionCommand {

    private final List<String> paths;
    private String separator = ";";

    ExecuteSqlScriptCommand(List<String> paths) {
        this.paths = paths;
    }

    public ExecuteSqlScriptCommand usingSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    @Override
    public void run(Connection connection) throws SQLException, IOException {
        try (Statement statement = connection.createStatement()) {

            for (String path : this.paths) {
                String content = this.clean(ResourceLoader.load(path).asString());

                for (String executable : this.split(content)) {
                    statement.execute(executable);
                }
            }

            if (!connection.getAutoCommit())
                connection.commit();
        }
    }

    private List<String> split(String content) {
        return Arrays.stream(content.split(Pattern.quote(this.separator)))
                .map(String::trim)
                .toList();
    }

    private String clean(String content) {
        return content.replaceAll("(?s)/\\*.*?\\*", "") //remove multiline comments /*...*/
                .replaceAll("--.*\\r?\\n?", "") //replace inline comments --
                .replaceAll("(?m)^\\s+", "") //remove multiple spaces or tabs at the beginning of the line
                .replaceAll("\\t+", "") //remove multiple tabs
                .replaceAll("\\s+", " ") //replace multiple spaces with single one
                .replaceAll("\\r?\\n", ""); //remove new line characters
    }
}

package com.github.ankowals.example.rest.framework.db;

import java.util.Arrays;

public class ExecuteSql {

  public static ExecuteSqlScriptCommand script(String... paths) {
    return new ExecuteSqlScriptCommand(Arrays.asList(paths));
  }

  public static ExecuteSqlStatementCommand statement(String statement) {
    return new ExecuteSqlStatementCommand(statement);
  }
}

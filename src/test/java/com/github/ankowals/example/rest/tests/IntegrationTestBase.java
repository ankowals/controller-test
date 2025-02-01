package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.environment.UsesPostgres;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTestBase implements UsesPostgres, TestPropertyProvider {

  @NonNull
  @Override
  public Map<String, String> getProperties() {
    return new HashMap<>(this.getPostgresProperties());
  }
}

package com.github.ankowals.example.rest.client;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.inject.Singleton;
import java.net.URI;

@Factory
public class ApiClients {

  @Bean
  @Primary
  @Singleton
  public ApiClient client(EmbeddedServer embeddedServer, JsonMapper jsonMapper) {
    return new ApiClient(() -> this.createBaseRequestSpec(embeddedServer.getURI(), jsonMapper));
  }

  private RequestSpecBuilder createBaseRequestSpec(URI baseUri, JsonMapper jsonMapper) {
    return new RequestSpecBuilder()
        .setBaseUri(baseUri)
        .addFilter(new RequestLoggingFilter())
        .addFilter(new ResponseLoggingFilter())
        .setConfig(
            RestAssuredConfig.config()
                .objectMapperConfig(
                    ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((cls, charset) -> jsonMapper)));
  }
}

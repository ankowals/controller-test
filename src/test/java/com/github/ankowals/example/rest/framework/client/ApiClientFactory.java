package com.github.ankowals.example.rest.framework.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankowals.example.rest.client.ApiClient;
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
public class ApiClientFactory {

    @Bean
    @Primary
    @Singleton
    public ApiClient client(EmbeddedServer embeddedServer) {
        return new ApiClient(() -> this.createBaseRequestSpec(embeddedServer.getURI()));
    }

    private RequestSpecBuilder createBaseRequestSpec(URI baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setConfig(RestAssuredConfig.config().objectMapperConfig(
                        ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory(
                                (cls, charset) -> this.createMapper())));
    }

    private ObjectMapper createMapper() {
        return JacksonMapperFactory.create(m ->
                m.setAnnotationIntrospector(JacksonMapperFactory.ignoreHiddenFieldsIntrospector()));
    }
}

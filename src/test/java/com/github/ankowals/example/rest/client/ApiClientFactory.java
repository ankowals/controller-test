package com.github.ankowals.example.rest.client;

import io.micronaut.context.annotation.Factory;
import io.micronaut.runtime.server.EmbeddedServer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import jakarta.inject.Singleton;

import java.net.URI;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

@Factory
public class ApiClientFactory {

    @Singleton
    public ApiClient client(EmbeddedServer embeddedServer) {
        return new ApiClient(() -> createBaseRequestSpec(embeddedServer.getURI()));
    }

    private RequestSpecBuilder createBaseRequestSpec(URI baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .setConfig(config().objectMapperConfig(
                        objectMapperConfig().jackson2ObjectMapperFactory(
                                (cls, charset) ->
                                        JacksonMapperFactory.create(
                                                m -> m.setAnnotationIntrospector(JacksonMapperFactory.ignoreHiddenFieldsIntrospector())))));
    }
}

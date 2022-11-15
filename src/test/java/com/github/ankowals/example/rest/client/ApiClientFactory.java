package com.github.ankowals.example.rest.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.net.URI;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

public class ApiClientFactory {

    public static ApiClient getClient(URI baseUri) {
        return new ApiClient(() -> createBaseRequestSpec(baseUri));
    }

    private static RequestSpecBuilder createBaseRequestSpec(URI baseUri) {
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

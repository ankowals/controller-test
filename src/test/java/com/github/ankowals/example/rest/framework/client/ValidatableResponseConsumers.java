package com.github.ankowals.example.rest.framework.client;

import io.micronaut.http.HttpStatus;
import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

public class ValidatableResponseConsumers {

    public static Consumer<ValidatableResponse> andExtractBecause(HttpStatus expectedStatusCode) {
        return response -> response.statusCode(expectedStatusCode.getCode());
    }
}

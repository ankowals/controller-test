package com.github.ankowals.example.rest.client;

import io.micronaut.http.HttpStatus;
import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

public class ValidatableResponseConsumers {

    public static Consumer<ValidatableResponse> andValidateStatusCodeIs(HttpStatus expectedStatusCode) {
        return andValidateStatusCodeIs(expectedStatusCode.getCode());
    }

    public static Consumer<ValidatableResponse> andValidateStatusCodeIs(int expectedStatusCode) {
        return response -> response.statusCode(expectedStatusCode);
    }
}

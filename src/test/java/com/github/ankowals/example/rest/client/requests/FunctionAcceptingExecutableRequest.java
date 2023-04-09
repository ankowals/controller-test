package com.github.ankowals.example.rest.client.requests;

import io.restassured.response.ValidatableResponse;

import java.util.function.Function;

public interface FunctionAcceptingExecutableRequest extends ExecutableRequest {
    default <R> R execute(Function<ValidatableResponse, R> expression) {
        return expression.apply(this.execute().then());
    }
}

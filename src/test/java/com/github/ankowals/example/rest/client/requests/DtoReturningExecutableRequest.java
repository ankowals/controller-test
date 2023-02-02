package com.github.ankowals.example.rest.client.requests;

import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

public interface DtoReturningExecutableRequest<T> extends ExecutableRequest {
    T execute(Consumer<ValidatableResponse> expression);
}

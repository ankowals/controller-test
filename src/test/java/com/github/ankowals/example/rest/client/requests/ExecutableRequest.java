package com.github.ankowals.example.rest.client.requests;

import io.restassured.response.Response;

@FunctionalInterface
public interface ExecutableRequest {
    Response execute();
}

package com.github.ankowals.example.rest.framework.client.requests;

import io.restassured.response.Response;

@FunctionalInterface
public interface ExecutableRequest {
    Response execute();
}

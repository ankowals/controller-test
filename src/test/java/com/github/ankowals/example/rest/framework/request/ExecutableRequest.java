package com.github.ankowals.example.rest.framework.request;

import io.restassured.response.Response;

@FunctionalInterface
public interface ExecutableRequest {
  Response execute();
}

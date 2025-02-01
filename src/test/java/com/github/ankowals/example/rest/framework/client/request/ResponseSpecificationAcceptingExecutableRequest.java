package com.github.ankowals.example.rest.framework.client.request;

import io.restassured.specification.ResponseSpecification;

public interface ResponseSpecificationAcceptingExecutableRequest<T> extends ExecutableRequest {
  T execute(ResponseSpecification responseSpecification);
}

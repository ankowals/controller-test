package com.github.ankowals.example.rest.framework.request;

import io.restassured.response.ValidatableResponse;
import java.util.function.Consumer;

public interface ConsumerAcceptingExecutableRequest<T> extends ExecutableRequest {
  T execute(Consumer<ValidatableResponse> expression);
}

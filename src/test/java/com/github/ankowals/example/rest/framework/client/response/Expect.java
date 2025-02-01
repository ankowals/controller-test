package com.github.ankowals.example.rest.framework.client.response;

import com.github.ankowals.example.rest.framework.client.dto.ErrorDto;
import io.micronaut.http.HttpStatus;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import java.util.List;
import java.util.function.Function;

public class Expect {

  public static Function<ValidatableResponse, List<ErrorDto>> error(HttpStatus expectedStatusCode) {
    return validatableResponse ->
        validatableResponse
            .statusCode(expectedStatusCode.getCode())
            .extract()
            .body()
            .jsonPath()
            .getList("_embedded.errors", ErrorDto.class);
  }

  public static ResponseSpecification status(HttpStatus httpStatus) {
    return new ResponseSpecBuilder().expectStatusCode(httpStatus.getCode()).build();
  }
}

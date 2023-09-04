package com.github.ankowals.example.rest.framework.client.response;

import io.micronaut.http.HttpStatus;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ExtractResponse {

    public static ResponseSpecification when(HttpStatus httpStatus) {
        return new ResponseSpecBuilder()
                .expectStatusCode(httpStatus.getCode())
                .build();
    }
}

package com.github.ankowals.example.rest.client;

import io.micronaut.http.HttpStatus;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecificationFactory {

    public static ResponseSpecification andExpectStatusCode(HttpStatus httpStatus) {
            return new ResponseSpecBuilder()
            .expectStatusCode(httpStatus.getCode())
            .build();
    }
}

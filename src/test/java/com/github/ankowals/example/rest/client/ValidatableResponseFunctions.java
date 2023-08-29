package com.github.ankowals.example.rest.client;

import com.github.ankowals.example.rest.client.dto.ErrorDto;
import io.micronaut.http.HttpStatus;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.function.Function;

public class ValidatableResponseFunctions {

    public static Function<ValidatableResponse, List<ErrorDto>> andExtractErrorsBecause(HttpStatus expectedStatusCode) {
        return validatableResponse -> validatableResponse.statusCode(expectedStatusCode.getCode())
                .extract().body()
                .jsonPath()
                .getList("_embedded.errors", ErrorDto.class);
    }
}

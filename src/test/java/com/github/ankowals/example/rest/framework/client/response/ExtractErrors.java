package com.github.ankowals.example.rest.framework.client.response;

import com.github.ankowals.example.rest.framework.client.dto.ErrorDto;
import io.micronaut.http.HttpStatus;
import io.restassured.response.ValidatableResponse;

import java.util.List;
import java.util.function.Function;

public class ExtractErrors {

    public static Function<ValidatableResponse, List<ErrorDto>> when(HttpStatus expectedStatusCode) {
        return validatableResponse -> validatableResponse.statusCode(expectedStatusCode.getCode())
                .extract().body()
                .jsonPath()
                .getList("_embedded.errors", ErrorDto.class);
    }
}

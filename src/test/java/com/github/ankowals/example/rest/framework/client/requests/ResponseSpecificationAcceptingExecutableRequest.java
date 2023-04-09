package com.github.ankowals.example.rest.framework.client.requests;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;

public interface ResponseSpecificationAcceptingExecutableRequest extends ExecutableRequest {
    default ValidatableResponse execute(ResponseSpecification responseSpecification) {
        return this.execute().then().spec(responseSpecification);
    }
}

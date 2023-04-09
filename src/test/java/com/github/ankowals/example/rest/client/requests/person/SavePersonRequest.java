package com.github.ankowals.example.rest.client.requests.person;

import com.github.ankowals.example.rest.client.requests.FunctionAcceptingExecutableRequest;
import com.github.ankowals.example.rest.client.requests.ResponseSpecificationAcceptingExecutableRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SavePersonRequest implements FunctionAcceptingExecutableRequest, ResponseSpecificationAcceptingExecutableRequest {

    private final RequestSpecBuilder requestSpecBuilder;

    public SavePersonRequest(RequestSpecBuilder requestSpecBuilder, PersonDto personDto) {
        this.requestSpecBuilder = requestSpecBuilder;
        this.requestSpecBuilder.setContentType("application/json");
        this.requestSpecBuilder.setAccept("application/json");
        this.requestSpecBuilder.setBody(personDto);
    }

    @Override
    public Response execute() {
        return given()
                .spec(this.requestSpecBuilder.build())
                .when()
                .post("/persons");
    }

}

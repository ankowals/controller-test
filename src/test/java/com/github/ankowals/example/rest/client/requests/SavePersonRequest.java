package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SavePersonRequest implements ExecutableRequest {

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
                .spec(requestSpecBuilder.build())
                .when()
                .post("/persons");
    }
}

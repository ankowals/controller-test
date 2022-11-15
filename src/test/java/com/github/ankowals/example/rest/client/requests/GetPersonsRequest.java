package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetPersonsRequest implements ExecutableRequest {

    private final RequestSpecBuilder requestSpecBuilder;

    public GetPersonsRequest(RequestSpecBuilder requestSpecBuilder) {
        this.requestSpecBuilder = requestSpecBuilder;
        this.requestSpecBuilder.setContentType("application/json");
    }

    @Override
    public Response execute() {
        return given()
                .spec(requestSpecBuilder.build())
                .when()
                .get("/persons");
    }

    public PersonDto[] asDto() {
        return execute()
                .as(PersonDto[].class, new Jackson2Mapper(((type, charset) -> JacksonMapperFactory.create())));
    }
}

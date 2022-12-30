package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetPersonRequest implements ExecutableRequest {

    private final RequestSpecBuilder requestSpecBuilder;

    public GetPersonRequest(RequestSpecBuilder requestSpecBuilder, Long id) {
        this.requestSpecBuilder = requestSpecBuilder;
        this.requestSpecBuilder.setContentType("application/json");
        this.requestSpecBuilder.addPathParam("id", id);
    }

    @Override
    public Response execute() {
        return given()
                .spec(requestSpecBuilder.build())
                .when()
                .get("/persons/{id}");
    }

    public PersonDto asDto() {
        return execute()
                .as(PersonDto.class, new Jackson2Mapper(((type, charset) -> JacksonMapperFactory.create())));
    }
}

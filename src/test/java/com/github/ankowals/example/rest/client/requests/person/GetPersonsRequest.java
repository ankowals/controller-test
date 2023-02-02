package com.github.ankowals.example.rest.client.requests.person;

import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.client.requests.DtoReturningExecutableRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

import static io.restassured.RestAssured.given;

public class GetPersonsRequest implements DtoReturningExecutableRequest<PersonDto[]> {

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

    @Override
    public PersonDto[] execute(Consumer<ValidatableResponse> expression) {
        Response response = execute();
        expression.accept(response.then());

        return response.as(PersonDto[].class,
                new Jackson2Mapper(((type, charset) -> JacksonMapperFactory.create())));
    }
}

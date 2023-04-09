package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.framework.client.requests.ConsumerAcceptingExecutableRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

import static io.restassured.RestAssured.given;

public class GetPersonsRequest implements ConsumerAcceptingExecutableRequest<PersonDto[]> {

    private final RequestSpecBuilder requestSpecBuilder;

    public GetPersonsRequest(RequestSpecBuilder requestSpecBuilder) {
        this.requestSpecBuilder = requestSpecBuilder;
        this.requestSpecBuilder.setContentType("application/json");
    }

    @Override
    public Response execute() {
        return given()
                .spec(this.requestSpecBuilder.build())
                .when()
                .get("/persons");
    }

    @Override
    public PersonDto[] execute(Consumer<ValidatableResponse> expression) {
        Response response = this.execute();
        expression.accept(response.then());

        return response.as(PersonDto[].class,
                new Jackson2Mapper(((type, charset) -> JacksonMapperFactory.create())));
    }
}

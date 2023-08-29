package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.framework.client.requests.ConsumerAcceptingExecutableRequest;
import com.github.ankowals.example.rest.framework.client.requests.ResponseSpecificationAcceptingExecutableRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.util.function.Consumer;

public class GetPersonRequest implements ConsumerAcceptingExecutableRequest<PersonDto>, ResponseSpecificationAcceptingExecutableRequest {

    private final RequestSpecBuilder requestSpecBuilder;

    public GetPersonRequest(RequestSpecBuilder requestSpecBuilder, Long id) {
        this.requestSpecBuilder = requestSpecBuilder;
        this.requestSpecBuilder.setContentType("application/json");
        this.requestSpecBuilder.addPathParam("id", id);
    }

    @Override
    public Response execute() {
        return RestAssured.given()
                .spec(this.requestSpecBuilder.build())
                .when()
                .get("/persons/{id}");
    }

    @Override
    public PersonDto execute(Consumer<ValidatableResponse> expression) {
        Response response = this.execute();
        expression.accept(response.then());

        return response.as(PersonDto.class);
    }
}

package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static io.restassured.RestAssured.given;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

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

    public Response executeUntil(Predicate<Response> predicate) {
        return executeUntil(predicate, 10, 1, TimeUnit.SECONDS);
    }

    public Response executeUntil(Predicate<Response> predicate, long timeout, long pollInterval, TimeUnit timeUnit) {
        Callable<Response> supplier = this::execute;
        return await().atMost(timeout, timeUnit)
                .pollInterval(pollInterval, timeUnit)
                .until(supplier, predicate);
    }
}

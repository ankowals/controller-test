package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.framework.JsonMapperFactory;
import com.github.ankowals.example.rest.framework.client.request.ResponseSpecificationAcceptingExecutableRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class GetPersonsRequest
    implements ResponseSpecificationAcceptingExecutableRequest<PersonDto[]> {

  private final RequestSpecBuilder requestSpecBuilder;

  public GetPersonsRequest(RequestSpecBuilder requestSpecBuilder) {
    this.requestSpecBuilder = requestSpecBuilder;
    this.requestSpecBuilder.setContentType("application/json");
  }

  @Override
  public Response execute() {
    return RestAssured.given().spec(this.requestSpecBuilder.build()).when().get("/persons");
  }

  @Override
  public PersonDto[] execute(ResponseSpecification responseSpecification) {
    return this.execute()
        .then()
        .spec(responseSpecification)
        .extract()
        .as(
            PersonDto[].class,
            new Jackson2Mapper(((type, charset) -> JsonMapperFactory.create())));
  }
}

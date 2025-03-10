package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.framework.request.ResponseSpecificationAcceptingExecutableRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class GetPersonRequest
    implements ResponseSpecificationAcceptingExecutableRequest<PersonDto> {

  private final RequestSpecBuilder requestSpecBuilder;

  public GetPersonRequest(RequestSpecBuilder requestSpecBuilder, Long id) {
    this.requestSpecBuilder = requestSpecBuilder;
    this.requestSpecBuilder.setContentType("application/json");
    this.requestSpecBuilder.addPathParam("id", id);
  }

  @Override
  public Response execute() {
    return RestAssured.given().spec(this.requestSpecBuilder.build()).when().get("/persons/{id}");
  }

  @Override
  public PersonDto execute(ResponseSpecification responseSpecification) {
    return this.execute().then().spec(responseSpecification).extract().as(PersonDto.class);
  }
}

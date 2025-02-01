package com.github.ankowals.example.rest.client.requests;

import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.framework.client.request.FunctionAcceptingExecutableRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

public class SavePersonRequest implements FunctionAcceptingExecutableRequest {

  private final RequestSpecBuilder requestSpecBuilder;

  public SavePersonRequest(RequestSpecBuilder requestSpecBuilder, PersonDto personDto) {
    this.requestSpecBuilder = requestSpecBuilder;
    this.requestSpecBuilder.setContentType("application/json");
    this.requestSpecBuilder.setAccept("application/json");
    this.requestSpecBuilder.setBody(personDto);
  }

  @Override
  public Response execute() {
    return RestAssured.given().spec(this.requestSpecBuilder.build()).when().post("/persons");
  }
}

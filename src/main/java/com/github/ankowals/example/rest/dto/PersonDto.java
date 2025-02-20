package com.github.ankowals.example.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class PersonDto {

  @JsonProperty("personName")
  private String name;

  @JsonProperty("personId")
  private Long id;

  @JsonProperty("personAge")
  private int age;

  public PersonDto() {}

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge(int age) {
    this.age = age;
  }
}

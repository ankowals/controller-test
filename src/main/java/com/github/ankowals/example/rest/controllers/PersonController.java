package com.github.ankowals.example.rest.controllers;

import com.github.ankowals.example.rest.domain.Message;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.services.PersonService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@Controller("/persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @Post()
  public HttpResponse<Message> savePerson(@Body @Valid PersonDto personDto) {
    this.personService.addPerson(personDto);
    return HttpResponse.status(HttpStatus.CREATED)
        .body(new Message(HttpStatus.CREATED.getCode(), "Saved successfully!"));
  }

  @Get(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<List<PersonDto>> getPersons() {
    return HttpResponse.status(HttpStatus.OK).body(this.personService.getPersons());
  }

  @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
  public HttpResponse<PersonDto> getPerson(@PathVariable Long id) {
    Optional<PersonDto> dto = this.personService.getPerson(id);
    return dto.isPresent()
        ? HttpResponse.status(HttpStatus.OK).body(dto.get())
        : HttpResponse.notFound();
  }
}

package com.github.ankowals.example.rest.controllers;

import com.github.ankowals.example.rest.domain.Message;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.services.PersonService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;

import javax.validation.Valid;

@Validated
@Controller("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Post()
    public HttpResponse<?> savePerson(@Body @Valid PersonDto personDto) {
        personService.addPerson(personDto);
        return HttpResponse.status(HttpStatus.CREATED)
                .body(new Message(HttpStatus.CREATED.getCode(),"Saved successfully!"));
    }

    @Get()
    public HttpResponse<?> getPersons() {
        return HttpResponse.status(HttpStatus.OK).body(personService.getPersons());
    }
}

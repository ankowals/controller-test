package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizationStrategy;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.ankowals.example.rest.assertions.ErrorDtoListAssertion.assertThatErrorsFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@MicronautTest(transactional = false, rollback = false)
public class SavePersonTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    PersonMapper personMapper;

    @Inject
    ApiClient api;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

    @Test
    void shouldPostPerson() throws IOException {
        Person person = personFactory.person("/person.json");
        PersonDto personDto = personMapper.toDto(person);

        api.savePerson(personDto)
                .execute()
                .then()
                .statusCode(HttpStatus.CREATED.getCode());

        assertThat(personRepository.findAll())
                .extracting(Person::getName, Person::getAge)
                .contains(tuple(personDto.getName(), personDto.getAge()));
    }

    @Test
    void shouldNotAcceptPersonWithEmptyName() {
        Person person = personFactory.person(p -> p.setName(""));

        Response response = api.savePerson(personMapper.toDto(person)).execute();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
        assertThatErrorsFrom(response).containMessages(
                "entity.name: can not be empty",
                "entity.name: size must be between 1 and 20");
    }
}

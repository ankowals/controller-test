package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.assertions.PersonDtoAssertion;
import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizationStrategy;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.github.ankowals.example.rest.client.ValidatableResponseConsumers.andValidateStatusCodeIs;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(transactional = false, rollback = false)
public class GetPersonsTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    ApiClient api;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

    @Test
    void shouldReturnPersons() {
        Stream.of(personFactory.person(),
                  personFactory.person(),
                  personFactory.person())
                .parallel()
                .forEach(person -> personRepository.save(person));

        PersonDto[] persons = api.getPersons().execute(andValidateStatusCodeIs(HttpStatus.OK));

        assertThat(persons)
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void shouldReturnPerson() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        PersonDto actual = api.getPerson(id).execute(andValidateStatusCodeIs(HttpStatus.OK));

        PersonDtoAssertion.assertThat(actual)
                .hasName(expected.getName())
                .isOfAge(expected.getAge());
    }

    @Test
    void shouldReturnNotFoundWhenWrongId() {
        api.getPerson(Long.MAX_VALUE)
                .execute()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.getCode());
    }
}
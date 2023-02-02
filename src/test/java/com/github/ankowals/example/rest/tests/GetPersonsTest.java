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

import java.util.List;

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
        List<Person> persons = List.of(personFactory.person(),
                personFactory.person(),
                personFactory.person());

        persons.forEach(person -> personRepository.save(person));

        PersonDto[] actualPersons = api.getPersons().execute(andValidateStatusCodeIs(HttpStatus.OK));

        assertThat(actualPersons)
                .extracting(PersonDto::getName)
                .containsAll(persons.stream().map(Person::getName).toList());
    }

    @Test
    void shouldReturnPerson() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        PersonDto actualPerson = api.getPerson(id).execute(andValidateStatusCodeIs(HttpStatus.OK));

        PersonDtoAssertion.assertThat(actualPerson)
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
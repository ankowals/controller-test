package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizer;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(transactional = false, rollback = false)
public class GetPersonsTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    ApiClient apiClient;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizer());

    @Test
    void shouldReturnPersons() {
        Stream.of(personFactory.person(),
                  personFactory.person(),
                  personFactory.person())
                .parallel()
                .forEach(person -> personRepository.save(person));

        assertThat(apiClient.getPersons().asDto())
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

        PersonDto actual = apiClient.getPerson(id).asDto();

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    void shouldReturnNotFoundWhenWrongId() {
        apiClient.getPerson(Long.MAX_VALUE)
                .execute()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.getCode());
    }
}
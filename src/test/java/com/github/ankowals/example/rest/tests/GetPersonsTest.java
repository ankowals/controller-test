package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.ApiClientFactory;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizer;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@MicronautTest(transactional = false, rollback = false)
public class GetPersonsTest extends TestBase {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    PersonRepository personRepository;

    ApiClient api;
    PersonFactory testData = new PersonFactory(new PersonRandomizer());

    @BeforeEach
    void setupApiClient() {
        api = ApiClientFactory.getClient(embeddedServer.getURI());
    }

    @Test
    void shouldReturnPersons() {
        Stream.of(testData.person(),
                  testData.person(),
                  testData.person())
                .parallel()
                .forEach(person -> personRepository.save(person));

        assertThat(api.getPersons().asDto())
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void shouldReturnPerson() {
        Person expected = testData.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        PersonDto actual = api.getPerson(id).asDto();

        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getAge()).isEqualTo(expected.getAge());
    }

    @Test
    void shouldReturnNotFoundWhenWrongId() {
        api.getPerson(Long.MAX_VALUE)
                .execute()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.getCode());
    }
}
package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.IntegrationTestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.ApiClientFactory;
import com.github.ankowals.example.rest.data.PersonDtoFactory;
import com.github.ankowals.example.rest.data.PersonDtoRandomizer;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.ankowals.example.rest.data.PersonDtoFactory.customize;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class SavePersonTest extends IntegrationTestBase {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    PersonRepository personRepository;

    ApiClient api;
    PersonDtoFactory testData = new PersonDtoFactory(new PersonDtoRandomizer());

    @BeforeEach
    void setupApiClient() {
        api = ApiClientFactory.getClient(embeddedServer.getURI());
    }

    @Test
    void shouldPostPerson() {
        PersonDto personDto = testData.person();

        api.savePerson(personDto)
                .execute()
                .then()
                .statusCode(HttpStatus.CREATED.getCode());

        assertThat(personRepository.findAll())
                .extracting(Person::getName, Person::getAge)
                .contains(Tuple.tuple(personDto.getName(), personDto.getAge()));
    }

    @Test
    void shouldNotAcceptPersonWithEmptyName() {
        PersonDto personDto = customize(testData.person(), person -> person.setName(""));

        api.savePerson(personDto)
                .execute()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }

    @Test
    void shouldConnectToDb() {
        assertThat(getPostgresConnection()).isNotNull();
    }
}

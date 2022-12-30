package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.ApiClientFactory;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizer;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.ankowals.example.rest.data.PersonFactory.customize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@MicronautTest(transactional = false, rollback = false)
public class SavePersonTest extends TestBase {

    @Inject
    EmbeddedServer embeddedServer;

    @Inject
    PersonRepository personRepository;

    @Inject
    PersonMapper mapper;

    ApiClient api;
    PersonFactory testData = new PersonFactory(new PersonRandomizer());

    @BeforeEach
    void setupApiClient() {
        api = ApiClientFactory.getClient(embeddedServer.getURI());
    }

    @Test
    void shouldPostPerson() {
        PersonDto personDto = mapper.toDto(testData.person());

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
        Person person = customize(testData.person(), p -> p.setName(""));

        api.savePerson(mapper.toDto(person))
                .execute()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.getCode());
    }
}

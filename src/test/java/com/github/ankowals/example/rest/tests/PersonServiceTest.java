package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.ApiClientFactory;
import com.github.ankowals.example.rest.data.PersonDtoFactory;
import com.github.ankowals.example.rest.data.PersonDtoRandomizer;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.environment.StartsPostgres;
import io.micronaut.http.HttpStatus;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static com.github.ankowals.example.rest.data.PersonDtoFactory.customize;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class PersonServiceTest implements StartsPostgres {

    @Inject
    EmbeddedServer embeddedServer;

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
    void shouldConnectToDb() throws SQLException {
        assertThat(getPostgresConnection()).isNotNull();
    }
}

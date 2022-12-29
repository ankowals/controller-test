package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.IntegrationTestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.ApiClientFactory;
import com.github.ankowals.example.rest.data.PersonDtoFactory;
import com.github.ankowals.example.rest.data.PersonDtoRandomizer;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class GetPersonsTest extends IntegrationTestBase {

    @Inject
    EmbeddedServer embeddedServer;

    ApiClient api;
    PersonDtoFactory testData = new PersonDtoFactory(new PersonDtoRandomizer());

    @BeforeEach
    void setupApiClient() {
        api = ApiClientFactory.getClient(embeddedServer.getURI());
    }

    @Test
    void shouldReturnPersons() {
        Stream.of(testData.person(), testData.person(), testData.person())
                .parallel().forEach(personDto -> api.savePerson(personDto).execute());

        assertThat(api.getPersons().asDto())
                .isNotEmpty()
                .hasSize(3);
    }
}
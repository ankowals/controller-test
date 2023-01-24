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
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.core.ConditionTimeoutException;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MicronautTest(transactional = false, rollback = false)
public class GetPersonsTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    ApiClient apiClient;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

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

        PersonDtoAssertion.assertThat(apiClient.getPerson(id).asDto())
                .hasName(expected.getName())
                .isOfAge(expected.getAge());
    }

    @Test
    void shouldReturnNotFoundWhenWrongId() {
        apiClient.getPerson(Long.MAX_VALUE)
                .execute()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.getCode());
    }
}
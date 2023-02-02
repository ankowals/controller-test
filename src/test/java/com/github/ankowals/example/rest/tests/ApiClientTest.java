package com.github.ankowals.example.rest.tests;

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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.core.ConditionTimeoutException;

import java.util.function.Predicate;

import static com.github.ankowals.example.rest.client.ValidatableResponseConsumers.andValidateStatusCodeIs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@MicronautTest(transactional = false, rollback = false)
public class ApiClientTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    ApiClient api;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

    @Test
    void shouldThrownConditionTimeoutException() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        assertThatExceptionOfType(ConditionTimeoutException.class)
                .isThrownBy(() -> api.getPerson(id).executeUntil(personNameEquals("terefere")));
    }

    @Test
    void shouldReturnPersonFulfillingCondition() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        Assertions.assertThatCode(() ->
                api.getPerson(id).executeUntil(personNameEquals(expected.getName()))).doesNotThrowAnyException();

        //alternatively
        await().untilAsserted(() -> {
            PersonDto actual = api.getPerson(id).execute(andValidateStatusCodeIs(HttpStatus.OK));
            assertThat(actual.getName()).isEqualTo(expected.getName());
        });
    }

    private Predicate<Response> personNameEquals(String expectedName) {
        return response -> response.as(PersonDto.class)
                .getName()
                .equals(expectedName);
    }
}

package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizationStrategy;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.core.ConditionTimeoutException;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@MicronautTest(transactional = false, rollback = false)
public class ApiClientTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    ApiClient apiClient;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

    @Test
    void shouldThrownConditionTimeoutException() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        Predicate<Response> predicate = response -> response.as(PersonDto.class).getName().equals("terefere");

        assertThatExceptionOfType(ConditionTimeoutException.class)
                .isThrownBy(() -> apiClient.getPerson(id).executeUntil(predicate));
    }

    @Test
    void shouldReturnPersonFulfillingCondition() {
        Person expected = personFactory.person();
        personRepository.save(expected);

        Long id = personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        Predicate<Response> predicate = response -> response.as(PersonDto.class).getName().equals(expected.getName());

        Assertions.assertThatCode(() -> apiClient.getPerson(id).executeUntil(predicate)).doesNotThrowAnyException();
    }
}

package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.assertions.PersonDtoAssertion;
import com.github.ankowals.example.rest.base.IntegrationTestBase;
import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.dto.ErrorDto;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.ankowals.example.rest.assertions.ErrorDtoListAssertion.assertThatErrorsFrom;
import static com.github.ankowals.example.rest.client.ResponseSpecificationFactory.andExpectStatusCode;
import static com.github.ankowals.example.rest.client.ValidatableResponseConsumers.andExtractBecause;
import static com.github.ankowals.example.rest.client.ValidatableResponseFunctions.andExtractErrorsBecause;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@MicronautTest(transactional = false, rollback = false)
public class PersonServiceTest extends IntegrationTestBase {

    @Inject
    PersonRepository personRepository;

    @Inject
    PersonMapper personMapper;

    @Inject
    ApiClient api;

    @Inject
    PersonFactory define;

    @Test
    void shouldSavePerson() throws IOException {
        Person person = this.define.person("/person.json");
        PersonDto personDto = this.personMapper.toDto(person);

        this.api.savePerson(personDto).execute(andExpectStatusCode(HttpStatus.CREATED));

        assertThat(this.personRepository.findAll())
                .extracting(Person::getName, Person::getAge)
                .contains(tuple(personDto.getName(), personDto.getAge()));
    }

    @Test
    void shouldGetPersons() {
        List<Person> persons = List.of(this.define.person(),
                this.define.person(),
                this.define.person());

        persons.forEach(person -> this.personRepository.save(person));

        PersonDto[] actual = this.api.getPersons().execute(andExtractBecause(HttpStatus.OK));

        assertThat(actual)
                .extracting(PersonDto::getName)
                .containsAll(persons.stream().map(Person::getName).toList());
    }

    @Test
    void shouldGetPersonAsynch() {
        Person expected = this.define.person();
        this.personRepository.save(expected);

        Long id = this.personRepository.findByName(expected.getName()).stream()
                .findAny()
                .orElseThrow()
                .getId();

        await().untilAsserted(() -> {
            PersonDto actual = this.api.getPerson(id).execute(andExtractBecause(HttpStatus.OK));
            PersonDtoAssertion.assertThat(actual)
                    .hasName(expected.getName())
                    .isOfAge(expected.getAge());
        });
    }

    @Test
    void shouldReturnNotFoundWhenWrongId() {
        this.api.getPerson(Long.MAX_VALUE).execute(andExpectStatusCode(HttpStatus.NOT_FOUND));
    }

    @Test
    void shouldNotAcceptPersonWithEmptyName() {
        Person person = this.define.person(p -> p.setName(""));
        PersonDto personDto = this.personMapper.toDto(person);

        List<ErrorDto> actual = this.api.savePerson(personDto)
                .execute(andExtractErrorsBecause(HttpStatus.BAD_REQUEST));

        assertThatErrorsFrom(actual).containMessages(
                "entity.name: can not be empty",
                "entity.name: size must be between 1 and 20");
    }

    @Test
    void shouldNotAcceptPersonWithNegativeAge() {
        Person person = this.define.person(p -> p.setAge(-1));
        PersonDto personDto = this.personMapper.toDto(person);

        List<ErrorDto> actual = this.api.savePerson(personDto)
                .execute(andExtractErrorsBecause(HttpStatus.BAD_REQUEST));

        assertThatErrorsFrom(actual).containMessages("entity.age: must be greater than or equal to 1");
    }
}

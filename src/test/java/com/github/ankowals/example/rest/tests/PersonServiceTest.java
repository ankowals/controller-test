package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.IntegrationTestBase;
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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.io.IOException;
import java.util.List;

import static com.github.ankowals.example.rest.framework.client.ResponseSpecificationFactory.andExpect;
import static com.github.ankowals.example.rest.framework.client.ValidatableResponseConsumers.andExtractBecause;
import static com.github.ankowals.example.rest.client.ValidatableResponseFunctions.andExtractErrorsBecause;
import static org.assertj.core.groups.Tuple.tuple;

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
        Person person = this.define.person("person.json");
        PersonDto personDto = this.personMapper.toDto(person);

        this.api.savePerson(personDto).execute(andExpect(HttpStatus.CREATED));

        Assertions.assertThat(this.personRepository.findAll())
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

        Assertions.assertThat(actual)
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

        Awaitility.await().untilAsserted(() -> {
            PersonDto actual = this.api.getPerson(id).execute(andExtractBecause(HttpStatus.OK));
            Assertions.assertThat(actual)
                            .returns(expected.getName(), PersonDto::getName)
                            .returns(expected.getAge(), PersonDto::getAge);
        });
    }

    //Sonar may return missing assertion warning
    @Test
    void shouldReturnNotFoundWhenWrongId() {
        this.api.getPerson(Long.MAX_VALUE).execute(andExpect(HttpStatus.NOT_FOUND));
    }

    @Test
    void shouldNotAcceptPersonWithEmptyName() {
        Person person = this.define.person(p -> p.setName(""));
        PersonDto personDto = this.personMapper.toDto(person);

        List<ErrorDto> actual = this.api.savePerson(personDto)
                .execute(andExtractErrorsBecause(HttpStatus.BAD_REQUEST));

        Assertions.assertThat(actual)
                .extracting(ErrorDto::getMessage)
                .contains("entity.name: can not be empty", "entity.name: size must be between 1 and 20");
    }

    @Test
    void shouldNotAcceptPersonWithNegativeAge() {
        Person person = this.define.person(p -> p.setAge(-1));
        PersonDto personDto = this.personMapper.toDto(person);

        List<ErrorDto> actual = this.api.savePerson(personDto)
                .execute(andExtractErrorsBecause(HttpStatus.BAD_REQUEST));

        Assertions.assertThat(actual)
                .extracting(ErrorDto::getMessage)
                .contains("entity.age: must be greater than or equal to 1");
    }
}

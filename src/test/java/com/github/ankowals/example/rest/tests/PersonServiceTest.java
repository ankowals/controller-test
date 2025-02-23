package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.client.ApiClient;
import com.github.ankowals.example.rest.client.response.ErrorDto;
import com.github.ankowals.example.rest.client.response.Expect;
import com.github.ankowals.example.rest.data.Persons;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

@MicronautTest(transactional = false, rollback = false)
class PersonServiceTest extends IntegrationTestBase {

  @Inject PersonRepository personRepository;

  @Inject PersonMapper personMapper;

  @Inject ApiClient api;

  @Inject
  Persons persons;

  @Test
  void shouldSavePerson() throws IOException {
    Person person = this.persons.fromJson("person.json");
    PersonDto personDto = this.personMapper.toDto(person);

    this.api.savePerson(personDto).execute();

    Assertions.assertThat(this.personRepository.findAll())
        .extracting(Person::getName, Person::getAge)
        .contains(Tuple.tuple(personDto.getName(), personDto.getAge()));
  }

  @Test
  void shouldGetPersons() {
    List<Person> personList =
        List.of(this.persons.randomOne(), this.persons.randomOne(), this.persons.randomOne());

    personList.forEach(person -> this.personRepository.save(person));

    PersonDto[] actual = this.api.getPersons().execute(Expect.status(HttpStatus.OK));

    Assertions.assertThat(actual)
        .extracting(PersonDto::getName)
        .containsAll(personList.stream().map(Person::getName).toList());
  }

  @Test
  void shouldGetPerson() {
    Person expected = this.persons.randomOne();
    this.personRepository.save(expected);

    Long id =
        this.personRepository.findByName(expected.getName()).stream()
            .findAny()
            .orElseThrow()
            .getId();

    Awaitility.await()
        .untilAsserted(
            () -> {
              PersonDto actual = this.api.getPerson(id).execute(Expect.status(HttpStatus.OK));
              Assertions.assertThat(actual)
                  .returns(expected.getName(), PersonDto::getName)
                  .returns(expected.getAge(), PersonDto::getAge);
            });
  }

  @Test
  void shouldReturnNotFoundWhenWrongId() {
    Response response = this.api.getPerson(Long.MAX_VALUE).execute();

    Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
  }

  @Test
  void shouldNotAcceptPersonWithEmptyName() {
    Person person = this.persons.randomOne(p -> p.setName(""));
    PersonDto personDto = this.personMapper.toDto(person);

    List<ErrorDto> actual =
        this.api.savePerson(personDto).execute(Expect.error(HttpStatus.BAD_REQUEST));

    Assertions.assertThat(actual)
        .extracting(ErrorDto::getMessage)
        .contains("name: can not be empty");
  }

  @Test
  void shouldNotAcceptPersonWithNegativeAge() {
    Person person = this.persons.randomOne(p -> p.setAge(-1));
    PersonDto personDto = this.personMapper.toDto(person);

    List<ErrorDto> actual =
        this.api.savePerson(personDto).execute(Expect.error(HttpStatus.BAD_REQUEST));

    Assertions.assertThat(actual)
        .extracting(ErrorDto::getMessage)
        .contains("age: must be greater than or equal to 1");
  }
}

package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, transactional = false, rollback = false)
class PersonRepositoryTest extends IntegrationTestBase {

  @Inject PersonRepository personRepository;

  @Inject PersonFactory define;

  @Test
  void shouldInsertPersons() throws IOException {
    List<Person> personsFormJson = this.define.personsFromJson("persons.json");
    personsFormJson.stream().parallel().forEach(person -> this.personRepository.save(person));

    List<Person> personsFromCsv = this.define.personsFromCsv("persons.csv");
    personsFromCsv.stream().parallel().forEach(person -> this.personRepository.save(person));

    List<Person> actual = this.personRepository.findAll();

    Assertions.assertThat(actual)
        .extracting(Person::getName)
        .containsAll(personsFormJson.stream().map(Person::getName).toList());

    Assertions.assertThat(actual)
        .extracting(Person::getName)
        .containsAll(personsFromCsv.stream().map(Person::getName).toList());
  }

  @Test
  void shouldReturnAdultPerson() {
    Stream.of(
            this.define.person(p -> p.setAge(7)),
            this.define.person(p -> p.setAge(17)),
            this.define.person(p -> p.setAge(77)))
        .parallel()
        .forEach(person -> this.personRepository.save(person));

    List<Person> actual = this.personRepository.findByAgeGreaterThan(17);

    Assertions.assertThat(actual)
        .isNotEmpty()
        .have(new Condition<>(p -> p.getAge() > 17, "be adult"));
  }

  @Test
  void shouldReturnZenek() {
    Stream.of(
            this.define.person(p -> p.setName("Waldek")),
            this.define.person(p -> p.setName("Zenek")),
            this.define.person(p -> p.setName("Ferdek")))
        .parallel()
        .forEach(person -> this.personRepository.save(person));

    List<Person> actual = this.personRepository.findZenek();

    Assertions.assertThat(actual)
        .isNotEmpty()
        .have(new Condition<>(p -> p.getName().equals("Zenek"), "named Zenek"));
  }

  @Test
  void shouldConnectToDb() {
    Assertions.assertThat(this.getPostgresConnection()).isNotNull();
  }
}

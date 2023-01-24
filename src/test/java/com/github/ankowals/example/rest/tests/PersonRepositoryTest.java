package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.base.TestBase;
import com.github.ankowals.example.rest.data.PersonFactory;
import com.github.ankowals.example.rest.data.PersonRandomizationStrategy;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false, transactional = false, rollback = false)
public class PersonRepositoryTest extends TestBase {

    @Inject
    PersonRepository personRepository;

    PersonFactory personFactory = new PersonFactory(new PersonRandomizationStrategy());

    @BeforeAll
    void seedDatabase() {
        Stream.of(personFactory.person(),
                  personFactory.person(p -> p.setName("Zenek")),
                  personFactory.person(p -> p.setAge(17)))
                .parallel().forEach(person -> personRepository.save(person));
    }

    @Test
    void shouldReturnAdultPerson() {
        List<Person> person = personRepository.findByAgeGreaterThan(17);

        assertThat(person).isNotEmpty()
                .have(new Condition<>(p -> p.getAge() > 17, "be adult"));
    }

    @Test
    void shouldReturnZenek() {
        List<Person> person = personRepository.findZenek();

        assertThat(person).isNotEmpty()
                .have(new Condition<>(p -> p.getName().equals("Zenek"), "named Zenek"));
    }

    @Test
    void shouldConnectToDb() {
        assertThat(getPostgresConnection()).isNotNull();
    }
}

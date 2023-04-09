package com.github.ankowals.example.rest.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.framework.data.RandomizationStrategy;
import io.micronaut.core.annotation.Creator;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Singleton
public class PersonFactory {

    private final RandomizationStrategy<Person> randomizationStrategy;
    private final ObjectMapper objectMapper = JacksonMapperFactory.create();

    @Creator
    public PersonFactory(RandomizationStrategy<Person> randomizationStrategy) {
        this.randomizationStrategy = randomizationStrategy;
    }

    public PersonFactory() {
        this.randomizationStrategy = person -> person;
    }

    public Person person() {
        return this.randomizationStrategy.randomize(new Person());
    }

    public Person person(Consumer<Person> customizer) {
        Person person = person();
        customizer.accept(person);

        return person;
    }

    public Person person(String filename) throws IOException {
        Person person = readResourceFileAs(filename, Person.class);
        return this.randomizationStrategy.randomize(person);
    }

    public List<Person> persons(String filename) throws IOException {
        List<Person> persons = List.of(readResourceFileAs(filename, Person[].class));
        persons.forEach(this.randomizationStrategy::randomize);

        return persons;
    }

    private <T> T readResourceFileAs(String filename, Class<T> clazz) throws IOException {
        return this.objectMapper.readValue(this.getClass().getResourceAsStream(filename), clazz);
    }
}

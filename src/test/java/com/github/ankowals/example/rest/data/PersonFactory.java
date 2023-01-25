package com.github.ankowals.example.rest.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankowals.example.rest.client.JacksonMapperFactory;
import com.github.ankowals.example.rest.domain.Person;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class PersonFactory {

    private final RandomizationStrategy<Person> randomizationStrategy;
    private final ObjectMapper objectMapper = JacksonMapperFactory.create();

    public PersonFactory(RandomizationStrategy<Person> randomizationStrategy) {
        this.randomizationStrategy = randomizationStrategy;
    }

    public PersonFactory() {
        this.randomizationStrategy = person -> person;
    }

    public Person person() {
        return randomizationStrategy.randomize(new Person());
    }

    public Person person(Consumer<Person> customizer) {
        Person person = person();
        customizer.accept(person);

        return person;
    }

    public Person personFrom(String filename) throws IOException {
        Person person = readResourceFileAs(filename, Person.class);
        return randomizationStrategy.randomize(person);
    }

    public List<Person> personsFrom(String filename) throws IOException {
        List<Person> persons = List.of(readResourceFileAs(filename, Person[].class));
        persons.forEach(randomizationStrategy::randomize);

        return persons;
    }

    private <T> T readResourceFileAs(String filename, Class<T> clazz) throws IOException {
        return objectMapper.readValue(this.getClass().getResourceAsStream(filename), clazz);
    }
}

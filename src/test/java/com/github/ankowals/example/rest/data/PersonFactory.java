package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.framework.data.RandomizationStrategy;
import com.github.ankowals.example.rest.framework.loaders.CsvLoader;
import com.github.ankowals.example.rest.framework.loaders.ResourceLoader;
import io.micronaut.core.annotation.Creator;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Singleton
public class PersonFactory {

    private final RandomizationStrategy<Person> randomizationStrategy;

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

    public Person person(String path) throws IOException {
        Person person = ResourceLoader.load(path).as(Person.class);
        return this.randomizationStrategy.randomize(person);
    }

    public List<Person> persons(String path) throws IOException {
        List<Person> persons = List.of(ResourceLoader.load(path).as(Person[].class));
        persons.forEach(this.randomizationStrategy::randomize);

        return persons;
    }

    public List<Person> personsFromCsv(String path) throws IOException {
        List<Person> persons = CsvLoader.load(path).asListOf(Person.class);
        persons.forEach(this.randomizationStrategy::randomize);

        return persons;
    }
}

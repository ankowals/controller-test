package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;

import java.util.function.Consumer;

public class PersonFactory {

    private final RandomizationStrategy<Person> randomizationStrategy;

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
}

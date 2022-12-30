package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;

import java.util.function.Consumer;

public class PersonFactory {

    private final RandomizationStrategy<Person> randomizationStrategy;

    public PersonFactory(RandomizationStrategy<Person> randomizationStrategy) {
        this.randomizationStrategy = randomizationStrategy;
    }

    public static Person customize(Person person, Consumer<Person> customizer) {
        customizer.accept(person);
        return person;
    }

    public Person person() {
        Person person = new Person();
        return randomizationStrategy.randomize(person);
    }
}

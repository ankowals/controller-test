package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.framework.data.RandomizationStrategy;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.function.Consumer;

@Singleton
public class PersonRandomizationStrategy implements RandomizationStrategy<Person> {

    private final List<Consumer<Person>> rules;

    public PersonRandomizationStrategy() {
        this.rules = List.of(
                p -> p.setName(this.english(11)),
                p -> p.setAge(this.between(1, 101)));
    }

    @Override
    public Person randomize(Person person) {
        this.rules.forEach(rule -> rule.accept(person));
        return person;
    }

    private String english(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private int between(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }
}

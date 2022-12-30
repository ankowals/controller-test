package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonRandomizer implements RandomizationStrategy<Person> {

    private final List<Consumer<Person>> rules = new ArrayList<>();

    public PersonRandomizer() {
        rules.add(p -> p.setName(english(11)));
        rules.add(p -> p.setAge(between(1, 101)));
    }

    @Override
    public Person randomize(Person person) {
        rules.forEach(rule -> rule.accept(person));

        return person;
    }

    private String english(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private int between(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }
}

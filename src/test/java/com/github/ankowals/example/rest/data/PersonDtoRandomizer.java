package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.dto.PersonDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonDtoRandomizer implements RandomizationStrategy <PersonDto> {

    List<Consumer<PersonDto>> rules = new ArrayList<>();

    @Override
    public PersonDto randomize(PersonDto personDto) {
        rules.add(dto -> dto.setName(english(11)));
        rules.add(dto -> dto.setAge(between(18, 101)));

        rules.forEach(rule -> rule.accept(personDto));

        return personDto;
    }

    private String english(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    private int between(int min, int max) {
        return RandomUtils.nextInt(min, max);
    }
}

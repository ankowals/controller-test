package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.dto.PersonDto;

import java.util.function.Consumer;

public class PersonDtoFactory {

    private final RandomizationStrategy<PersonDto> randomizationStrategy;

    public PersonDtoFactory(RandomizationStrategy<PersonDto> randomizationStrategy) {
        this.randomizationStrategy = randomizationStrategy;
    }

    public static PersonDto customize(PersonDto personDto, Consumer<PersonDto> customizer) {
        customizer.accept(personDto);
        return personDto;
    }

    public PersonDto person() {
        PersonDto personDto = new PersonDto();
        return randomizationStrategy.randomize(personDto);
    }

}

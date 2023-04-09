package com.github.ankowals.example.rest.assertions;

import com.github.ankowals.example.rest.dto.PersonDto;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PersonDtoAssertion extends AbstractAssert<PersonDtoAssertion, PersonDto> {

    private PersonDtoAssertion(PersonDto personDto) {
        super(personDto, PersonDtoAssertion.class);
    }

    public static PersonDtoAssertion assertThat(PersonDto personDto) {
        return new PersonDtoAssertion(personDto);
    }

    public PersonDtoAssertion hasName(String name) {
        Assertions.assertThat(this.actual.getName()).isEqualTo(name);
        return this;
    }

    public PersonDtoAssertion isOfAge(int age) {
        Assertions.assertThat(this.actual.getAge()).isEqualTo(age);
        return this;
    }

}

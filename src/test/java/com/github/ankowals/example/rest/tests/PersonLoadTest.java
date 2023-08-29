package com.github.ankowals.example.rest.tests;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.framework.loaders.CsvLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class PersonLoadTest {

    @Test
    void shouldLoadPersonFromCsv() throws IOException {
        List<Person> persons = CsvLoader.load("persons.csv").asListOf(Person.class);

        Assertions.assertThat(persons)
                .extracting(Person::getName)
                .contains("Macius", "Olek", "Wojtek", "Marek", "Robert");
    }
}

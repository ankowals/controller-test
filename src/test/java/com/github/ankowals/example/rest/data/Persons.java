package com.github.ankowals.example.rest.data;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.framework.data.RandomizationStrategy;
import com.github.ankowals.example.rest.framework.loaders.CsvLoader;
import com.github.ankowals.example.rest.framework.loaders.JsonLoader;
import io.micronaut.core.annotation.Creator;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Singleton
public class Persons {

  private final RandomizationStrategy<Person> randomizationStrategy;
  private final JsonLoader jsonLoader;
  private final CsvLoader csvLoader;

  @Creator
  public Persons(RandomizationStrategy<Person> randomizationStrategy, JsonLoader jsonLoader, CsvLoader csvLoader) {
    this.randomizationStrategy = randomizationStrategy;
    this.jsonLoader = jsonLoader;
    this.csvLoader = csvLoader;
  }

  public Persons(JsonLoader jsonLoader, CsvLoader csvLoader) {
    this(person -> person, jsonLoader, csvLoader);
  }

  public Person randomOne() {
    return this.randomizationStrategy.randomize(new Person());
  }

  public Person randomOne(Consumer<Person> customizer) {
    Person person = this.randomOne();
    customizer.accept(person);

    return person;
  }

  public Person fromJson(String path) throws IOException {
    Person person = this.jsonLoader.load(path).as(Person.class);
    return this.randomizationStrategy.randomize(person);
  }

  public List<Person> manyFromJson(String path) throws IOException {
    List<Person> persons = List.of(this.jsonLoader.load(path).as(Person[].class));
    persons.forEach(this.randomizationStrategy::randomize);

    return persons;
  }

  public List<Person> fromCsv(String path) throws IOException {
    List<Person> persons = this.csvLoader.load(path).asListOf(Person.class);
    persons.forEach(this.randomizationStrategy::randomize);

    return persons;
  }
}

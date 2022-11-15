package com.github.ankowals.example.rest.repositories;

import com.github.ankowals.example.rest.domain.Person;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}

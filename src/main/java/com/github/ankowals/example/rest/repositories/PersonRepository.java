package com.github.ankowals.example.rest.repositories;

import com.github.ankowals.example.rest.domain.Person;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

    @Override
    List<Person> findAll();

    List<Person> findByName(String name);

    List<Person> findByAgeGreaterThan(int age);

    @Query(value =  "SELECT * FROM person p WHERE p.name = 'Zenek' ORDER BY p.age DESC", nativeQuery = true)
    List<Person> findZenek();
}

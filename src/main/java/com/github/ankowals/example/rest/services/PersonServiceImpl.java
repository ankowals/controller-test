package com.github.ankowals.example.rest.services;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Singleton
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonServiceImpl(PersonRepository repository, PersonMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PersonDto> getPerson(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public PersonDto addPerson(PersonDto personDto) {
        Person person = mapper.toPerson(personDto);
        person.setId(null);

        Person saved = repository.save(person);

        return mapper.toDto(saved);
    }

    @Override
    public List<PersonDto> getPersons() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}

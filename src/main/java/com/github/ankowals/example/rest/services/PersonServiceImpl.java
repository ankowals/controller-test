package com.github.ankowals.example.rest.services;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import com.github.ankowals.example.rest.mappers.PersonMapper;
import com.github.ankowals.example.rest.repositories.PersonRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

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
        return this.repository.findById(id)
                .map(this.mapper::toDto);
    }

    @Override
    public PersonDto addPerson(PersonDto personDto) {
        Person person = this.mapper.toPerson(personDto);
        person.setId(null);

        return this.mapper.toDto(this.repository.save(person));
    }

    @Override
    public List<PersonDto> getPersons() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::toDto)
                .toList();
    }
}

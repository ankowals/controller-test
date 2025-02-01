package com.github.ankowals.example.rest.services;

import com.github.ankowals.example.rest.dto.PersonDto;
import java.util.List;
import java.util.Optional;

public interface PersonService {
  Optional<PersonDto> getPerson(Long id);

  void addPerson(PersonDto personDto);

  List<PersonDto> getPersons();
}

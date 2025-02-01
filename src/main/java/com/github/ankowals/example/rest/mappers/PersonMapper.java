package com.github.ankowals.example.rest.mappers;

import com.github.ankowals.example.rest.domain.Person;
import com.github.ankowals.example.rest.dto.PersonDto;
import jakarta.inject.Singleton;
import org.mapstruct.Mapper;

@Singleton
@Mapper(componentModel = "jsr330")
public interface PersonMapper {
  Person toPerson(PersonDto personDto);

  PersonDto toDto(Person person);
}

package com.github.ankowals.example.rest.client;

import com.github.ankowals.example.rest.client.requests.person.GetPersonRequest;
import com.github.ankowals.example.rest.client.requests.person.GetPersonsRequest;
import com.github.ankowals.example.rest.client.requests.person.SavePersonRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;

import java.util.function.Supplier;

public class ApiClient {

    private final Supplier<RequestSpecBuilder> requestSpecBuilderSupplier;

    public ApiClient(Supplier<RequestSpecBuilder> requestSpecBuilderSupplier) {
        this.requestSpecBuilderSupplier = requestSpecBuilderSupplier;
    }

    public SavePersonRequest savePerson(PersonDto personDto) {
        return new SavePersonRequest(requestSpecBuilderSupplier.get(), personDto);
    }

    public GetPersonsRequest getPersons() {
        return new GetPersonsRequest(requestSpecBuilderSupplier.get());
    }

    public GetPersonRequest getPerson(Long id) {
        return new GetPersonRequest(requestSpecBuilderSupplier.get(), id);
    }
}

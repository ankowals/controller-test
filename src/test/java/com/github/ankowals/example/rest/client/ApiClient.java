package com.github.ankowals.example.rest.client;

import com.github.ankowals.example.rest.client.requests.GetPersonRequest;
import com.github.ankowals.example.rest.client.requests.GetPersonsRequest;
import com.github.ankowals.example.rest.client.requests.SavePersonRequest;
import com.github.ankowals.example.rest.dto.PersonDto;
import io.restassured.builder.RequestSpecBuilder;

import java.util.function.Supplier;

public class ApiClient {

    private final Supplier<RequestSpecBuilder> requestSpecBuilderSupplier;

    public ApiClient(Supplier<RequestSpecBuilder> requestSpecBuilderSupplier) {
        this.requestSpecBuilderSupplier = requestSpecBuilderSupplier;
    }

    public SavePersonRequest savePerson(PersonDto personDto) {
        return new SavePersonRequest(this.requestSpecBuilderSupplier.get(), personDto);
    }

    public GetPersonsRequest getPersons() {
        return new GetPersonsRequest(this.requestSpecBuilderSupplier.get());
    }

    public GetPersonRequest getPerson(Long id) {
        return new GetPersonRequest(this.requestSpecBuilderSupplier.get(), id);
    }
}

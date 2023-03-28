package com.github.ankowals.example.rest.assertions;

import io.restassured.response.Response;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.Assertions;

import java.util.List;

public class ErrorDtoListAssertion extends AbstractListAssert<ErrorDtoListAssertion, List<ErrorDto>, ErrorDto, ErrorDtoListAssertion.ErrorDtoAssertion> {

    public ErrorDtoListAssertion(List<ErrorDto> errors, Class<?> selfType) {
        super(errors, selfType);
    }

    public static ErrorDtoListAssertion assertThat(List<ErrorDto> actual) {
        return new ErrorDtoListAssertion(actual, ErrorDtoListAssertion.class);
    }

    public static ErrorDtoListAssertion assertThatErrorsFrom(Response response) {
        List<ErrorDto> actual = response.getBody().jsonPath().getList("_embedded.errors", ErrorDto.class);
        return new ErrorDtoListAssertion(actual, ErrorDtoListAssertion.class);
    }

    public ErrorDtoListAssertion containMessages(String... messages) {
        Assertions.assertThat(actual)
                .extracting(ErrorDto::getMessage)
                .contains(messages);

        return this;
    }

    @Override
    protected ErrorDtoAssertion toAssert(ErrorDto value, String description) {
        return null;
    }

    @Override
    protected ErrorDtoListAssertion newAbstractIterableAssert(Iterable<? extends ErrorDto> iterable) {
        return null;
    }

    static class ErrorDtoAssertion extends AbstractAssert<ErrorDtoAssertion, ErrorDto> {
        public ErrorDtoAssertion(ErrorDto error, Class<?> selfType) {
            super(error, selfType);
        }
    }
}

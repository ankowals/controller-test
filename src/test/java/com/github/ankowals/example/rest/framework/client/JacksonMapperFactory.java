package com.github.ankowals.example.rest.framework.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.function.Consumer;

public class JacksonMapperFactory {

    public static ObjectMapper create(Consumer<ObjectMapper> customizer) {
        ObjectMapper objectMapper = create();
        customizer.accept(objectMapper);

        return objectMapper;
    }

    public static ObjectMapper create() {
            ObjectMapper om = new ObjectMapper().findAndRegisterModules();

            om.registerModule(new JavaTimeModule());
            om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return om;
    }

    public static JacksonAnnotationIntrospector ignoreHiddenFieldsIntrospector() {
        return new JacksonAnnotationIntrospector() {
            @Override
            public boolean hasIgnoreMarker(final AnnotatedMember m) {
                return m.hasAnnotation(Hidden.class) || super.hasIgnoreMarker(m);
            }
        };
    }

}

package com.github.ankowals.example.rest.framework;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.function.Consumer;

public class JsonMapperFactory {

  public static JsonMapper create(Consumer<JsonMapper> customizer) {
    JsonMapper jsonMapper = create();
    customizer.accept(jsonMapper);

    return jsonMapper;
  }

  public static JsonMapper create() {
    JsonMapper jsonMapper = new JsonMapper();

    jsonMapper.findAndRegisterModules();

    jsonMapper.registerModule(new JavaTimeModule());
    jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    return jsonMapper;
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

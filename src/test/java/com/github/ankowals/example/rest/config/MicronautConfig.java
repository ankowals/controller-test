package com.github.ankowals.example.rest.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.ankowals.example.rest.framework.CsvMapperFactory;
import com.github.ankowals.example.rest.framework.JsonMapperFactory;
import com.github.ankowals.example.rest.framework.loaders.CsvLoader;
import com.github.ankowals.example.rest.framework.loaders.JsonLoader;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class MicronautConfig {

    @Singleton
    CsvLoader csvLoader() {
        return new CsvLoader(CsvMapperFactory.create());
    }

    @Singleton
    JsonLoader jsonLoader(JsonMapper jsonMapper) {
        return new JsonLoader(jsonMapper);
    }

    @Singleton
    JsonMapper createMapper() {
        return JsonMapperFactory.create(
                m -> m.setAnnotationIntrospector(JsonMapperFactory.ignoreHiddenFieldsIntrospector()));
    }
}

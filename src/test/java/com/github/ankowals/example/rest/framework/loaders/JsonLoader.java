package com.github.ankowals.example.rest.framework.loaders;

import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.*;

public class JsonLoader {

  private final JsonMapper jsonMapper;
  private final ResourceLoader resourceLoader;
  private String path;

  public JsonLoader(JsonMapper jsonMapper) {
    this.jsonMapper = jsonMapper;
    this.resourceLoader = new ResourceLoader();
  }

  public JsonLoader load(String path) {
    this.path = path;
    return this;
  }

  public <T> T as(Class<T> clazz) throws IOException {
    return this.jsonMapper.readValue(this.resourceLoader.asString(this.path), clazz);
  }
}

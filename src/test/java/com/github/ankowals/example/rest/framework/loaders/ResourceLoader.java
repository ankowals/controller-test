package com.github.ankowals.example.rest.framework.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankowals.example.rest.framework.client.JacksonMapperFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceLoader {

  private static final ObjectMapper OBJECT_MAPPER = JacksonMapperFactory.create();

  private final String path;

  private ResourceLoader(String path) {
    this.path = path;
  }

  public static ResourceLoader load(String path) {
    return new ResourceLoader(path);
  }

  public InputStream asInputStream() {
    return ClassLoader.getSystemClassLoader().getResourceAsStream(this.path);
  }

  public String asString() throws IOException {
    try (InputStream inputStream = this.asInputStream()) {

      if (inputStream == null)
        throw new FileNotFoundException(String.format("Resource file '%s' not found!", this.path));

      try (InputStreamReader inputStreamReader =
              new InputStreamReader(inputStream, StandardCharsets.UTF_8);
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
        return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    }
  }

  public <T> T as(Class<T> clazz) throws IOException {
    try (InputStream inputStream = this.asInputStream()) {

      if (inputStream == null)
        throw new FileNotFoundException(String.format("Resource file '%s' not found!", this.path));

      return OBJECT_MAPPER.readValue(inputStream, clazz);
    }
  }
}

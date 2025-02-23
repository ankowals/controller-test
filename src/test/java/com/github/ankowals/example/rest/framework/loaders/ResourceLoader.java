package com.github.ankowals.example.rest.framework.loaders;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class ResourceLoader {

  public String asString(String path) throws IOException {
    try (InputStream inputStream = this.toInputStream(path);
         InputStreamReader inputStreamReader =
            new InputStreamReader(inputStream, StandardCharsets.UTF_8);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

  private InputStream toInputStream(String path) throws FileNotFoundException {
    InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(path);

    if (inputStream == null) {
      throw new FileNotFoundException(String.format("Resource file '%s' not found!", path));
    }

    return inputStream;
  }
}

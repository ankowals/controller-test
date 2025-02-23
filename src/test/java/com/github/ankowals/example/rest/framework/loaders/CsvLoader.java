package com.github.ankowals.example.rest.framework.loaders;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

  private final CsvMapper csvMapper;
  private final ResourceLoader resourceLoader;
  private CsvSchema csvSchema;
  private String path;

  public CsvLoader(CsvMapper csvMapper) {
    this.csvMapper = csvMapper;
    this.resourceLoader = new ResourceLoader();
    this.csvSchema = CsvSchema.emptySchema().withHeader().withColumnReordering(true);
  }

  public CsvLoader load(String path) {
    this.path = path;
    return this;
  }

  public CsvLoader usingSchema(CsvSchema csvSchema) {
    this.csvSchema = csvSchema;
    return this;
  }

  public <T> List<T> asListOf(Class<T> as) throws IOException {
    List<T> results = new ArrayList<>();

    try (MappingIterator<T> mappingIterator =
        this.csvMapper
            .readerFor(as)
            .with(this.csvSchema)
            .readValues(this.resourceLoader.asString(this.path))) {
      while (mappingIterator.hasNext()) {
        results.add(mappingIterator.nextValue());
      }
    }

    return results;
  }
}

package com.github.ankowals.example.rest.framework.loaders;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    private static final CsvMapper CSV_MAPPER = new CsvMapper();

    private final String path;
    private CsvSchema csvSchema;

    private CsvLoader(String path) {
        this.path = path;
        this.csvSchema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnReordering(true);

        CSV_MAPPER.configure(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS, false);
        CSV_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static CsvLoader load(String path) {
        return new CsvLoader(path);
    }

    public CsvLoader usingSchema(CsvSchema csvSchema) {
        this.csvSchema = csvSchema;
        return this;
    }

    public <T> List<T> asListOf(Class<T> as) throws IOException {
        List<T> results = new ArrayList<>();

        try (InputStream inputStream = ResourceLoader.load(this.path).asInputStream()) {

            if (inputStream == null)
                throw new FileNotFoundException(String.format("Resource file '%s' not found!", this.path));

            MappingIterator<T> mappingIterator = CSV_MAPPER.readerFor(as)
                    .with(this.csvSchema)
                    .readValues(inputStream);

            while (mappingIterator.hasNext()) {
                results.add(mappingIterator.nextValue());
            }
        }

        return results;
    }

}

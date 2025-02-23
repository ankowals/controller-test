package com.github.ankowals.example.rest.framework;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

public class CsvMapperFactory {

    public static CsvMapper create() {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS, false);
        csvMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        return csvMapper;
    }
}

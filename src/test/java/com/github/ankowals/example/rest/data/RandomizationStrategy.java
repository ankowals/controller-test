package com.github.ankowals.example.rest.data;

@FunctionalInterface
public interface RandomizationStrategy<T> {
    T randomize(T t);
}

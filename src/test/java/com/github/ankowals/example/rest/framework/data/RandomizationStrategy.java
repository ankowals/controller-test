package com.github.ankowals.example.rest.framework.data;

@FunctionalInterface
public interface RandomizationStrategy<T> {
  T randomize(T t);
}

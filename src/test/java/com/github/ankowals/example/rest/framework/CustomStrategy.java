package com.github.ankowals.example.rest.framework;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

public class CustomStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();

    @Override
    public int getParallelism() {
        return NUMBER_OF_THREADS;
    }

    @Override
    public int getMinimumRunnable() {
        return 0;
    }

    @Override
    public int getMaxPoolSize() {
        return NUMBER_OF_THREADS;
    }

    @Override
    public int getCorePoolSize() {
        return NUMBER_OF_THREADS;
    }

    @Override
    public int getKeepAliveSeconds() {
        return 60;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}

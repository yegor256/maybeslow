/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * Minimal stub that satisfies the two methods MayBeSlow.beforeEach
 * actually calls: getTestMethod() and getUniqueId().
 *
 * @since 0.2.0
 */
@SuppressWarnings("PMD.CouplingBetweenObjects")
final class StubExtensionContext implements ExtensionContext {

    /**
     * Store for this context.
     */
    private final Store store = new ThreadStore();

    @Override
    public Optional<ExtensionContext> getParent() {
        return Optional.empty();
    }

    @Override
    public ExtensionContext getRoot() {
        return this;
    }

    @Override
    public String getUniqueId() {
        return "stub/[1]";
    }

    @Override
    public String getDisplayName() {
        return "stub";
    }

    @Override
    public Set<String> getTags() {
        return Set.of();
    }

    @Override
    public Optional<AnnotatedElement> getElement() {
        return Optional.empty();
    }

    @Override
    public Optional<Class<?>> getTestClass() {
        return Optional.empty();
    }

    @Override
    public List<Class<?>> getEnclosingTestClasses() {
        return List.of();
    }

    @Override
    public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
        return Optional.empty();
    }

    @Override
    public Optional<Object> getTestInstance() {
        return Optional.empty();
    }

    @Override
    public Optional<TestInstances> getTestInstances() {
        return Optional.empty();
    }

    @Override
    public Optional<Method> getTestMethod() {
        try {
            return Optional.of(
                MayBeSlowConcurrencyTest.class.getDeclaredMethod(
                    "doesNotThrowWhenCalledConcurrently"
                )
            );
        } catch (final NoSuchMethodException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Optional<Throwable> getExecutionException() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getConfigurationParameter(final String key) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getConfigurationParameter(
        final String key,
        final Function<? super String, ? extends T> transformer
    ) {
        return Optional.empty();
    }

    @Override
    public void publishReportEntry(final Map<String, String> map) {
        // nothing to publish in stub
    }

    @Override
    public void publishFile(
        final String name,
        final org.junit.jupiter.api.MediaType type,
        final ThrowingConsumer<Path> action
    ) {
        // nothing to publish in stub
    }

    @Override
    public void publishDirectory(
        final String name,
        final ThrowingConsumer<Path> action
    ) {
        // nothing to publish in stub
    }

    @Override
    public Store getStore(final Namespace namespace) {
        return this.store;
    }

    @Override
    public Store getStore(final StoreScope scope, final Namespace namespace) {
        return this.store;
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.CONCURRENT;
    }

    @Override
    public ExecutableInvoker getExecutableInvoker() {
        throw new UnsupportedOperationException();
    }
}

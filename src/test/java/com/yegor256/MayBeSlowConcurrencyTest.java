/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import com.yegor256.Together;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * Regression test for the thread-reuse bug: MayBeSlow throws
 * IllegalThreadStateException when beforeEach is called concurrently from
 * multiple threads, because the shared {@code watch} field is assigned and
 * started in two separate, non-atomic steps.
 */
final class MayBeSlowConcurrencyTest {

    @Test
    @Disabled("Reproduces the bug: Thread watch field is not safe for concurrent beforeEach calls")
    void doesNotThrowWhenCalledConcurrently() throws Exception {
        for (int round = 0; round < 20; round++) {
            final MayBeSlow extension = new MayBeSlow();
            new Together<>(
                50,
                thread -> {
                    final ExtensionContext ctx = new StubExtensionContext();
                    extension.beforeEach(ctx);
                    extension.afterEach(ctx);
                    return true;
                }
            ).asList();
        }
    }

    /**
     * Minimal stub that satisfies the two methods MayBeSlow.beforeEach
     * actually calls: getTestMethod() and getUniqueId().
     */
    @SuppressWarnings("PMD.TooManyMethods")
    private static final class StubExtensionContext implements ExtensionContext {

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
        }

        @Override
        public void publishFile(
            final String name,
            final org.junit.jupiter.api.MediaType type,
            final ThrowingConsumer<Path> action
        ) {
        }

        @Override
        public void publishDirectory(
            final String name,
            final ThrowingConsumer<Path> action
        ) {
        }

        @Override
        public Store getStore(final Namespace namespace) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Store getStore(final StoreScope scope, final Namespace namespace) {
            throw new UnsupportedOperationException();
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
}

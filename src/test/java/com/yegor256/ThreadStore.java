/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * In-memory {@link ExtensionContext.Store} backed by a {@link ConcurrentHashMap}.
 *
 * @since 0.2.0
 */
final class ThreadStore implements ExtensionContext.Store {

    /**
     * Backing storage.
     */
    private final Map<Object, Object> data = new ConcurrentHashMap<>();

    @Override
    public Object get(final Object key) {
        return this.data.get(key);
    }

    @Override
    public <V> V get(final Object key, final Class<V> type) {
        return type.cast(this.data.get(key));
    }

    @Override
    @SuppressWarnings("deprecation")
    public <K, V> Object getOrComputeIfAbsent(
        final K key,
        final Function<? super K, ? extends V> creator
    ) {
        return this.data.computeIfAbsent(key, ignored -> creator.apply(key));
    }

    @Override
    public <K, V> Object computeIfAbsent(
        final K key,
        final Function<? super K, ? extends V> creator
    ) {
        return this.data.computeIfAbsent(key, ignored -> creator.apply(key));
    }

    @Override
    @SuppressWarnings("deprecation")
    public <K, V> V getOrComputeIfAbsent(
        final K key,
        final Function<? super K, ? extends V> creator,
        final Class<V> type
    ) {
        return type.cast(this.data.computeIfAbsent(key, ignored -> creator.apply(key)));
    }

    @Override
    public <K, V> V computeIfAbsent(
        final K key,
        final Function<? super K, ? extends V> creator,
        final Class<V> type
    ) {
        return type.cast(this.data.computeIfAbsent(key, ignored -> creator.apply(key)));
    }

    @Override
    public void put(final Object key, final Object value) {
        this.data.put(key, value);
    }

    @Override
    public Object remove(final Object key) {
        return this.data.remove(key);
    }

    @Override
    public <V> V remove(final Object key, final Class<V> type) {
        return type.cast(this.data.remove(key));
    }
}

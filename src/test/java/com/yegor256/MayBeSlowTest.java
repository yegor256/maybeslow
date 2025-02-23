/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test case for {@link MayBeSlow}.
 *
 * @since 0.1.0
 */
final class MayBeSlowTest {

    @Test
    @ExtendWith(MayBeSlow.class)
    void slowTest() throws InterruptedException {
        Thread.sleep(5000L);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 3, 1, 7})
    @ExtendWith(MayBeSlow.class)
    void sleepsFewSeconds(final int seconds) throws InterruptedException {
        Thread.sleep((long) seconds * 1000L);
    }
}

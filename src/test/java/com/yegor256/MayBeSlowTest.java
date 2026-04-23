/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import org.junit.jupiter.api.Assertions;
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
    void slowTest() {
        Assertions.assertDoesNotThrow(() -> Thread.sleep(5000L));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 3, 1, 7})
    @ExtendWith(MayBeSlow.class)
    void sleepsFewSeconds(final int seconds) {
        Assertions.assertDoesNotThrow(() -> Thread.sleep(seconds * 1000L));
    }
}

/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Regression test for the thread-reuse bug: MayBeSlow throws
 * IllegalThreadStateException when beforeEach is called concurrently from
 * multiple threads, because the shared {@code watch} field is assigned and
 * started in two separate, non-atomic steps.
 *
 * @since 0.2.0
 */
final class MayBeSlowConcurrencyTest {

    @RepeatedTest(20)
    void doesNotThrowWhenCalledConcurrently() throws Exception {
        final MayBeSlow extension = new MayBeSlow();
        Assertions.assertFalse(
            new Together<>(
                50,
                thread -> {
                    final ExtensionContext ctx = new StubExtensionContext();
                    extension.beforeEach(ctx);
                    extension.afterEach(ctx);
                    return true;
                }
            ).asList().isEmpty()
        );
    }
}

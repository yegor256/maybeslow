/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test case for {@link MayBeSlow} with a parameterized test
 * in a nested class.
 *
 * @since 0.73.4
 */
final class MayBeSlowNestedTest {

    /**
     * Sink for warning messages logged by {@link MayBeSlow}.
     *
     * @since 0.73.4
     */
    private static final StringWriter SINK = new StringWriter();

    /**
     * Ctor.
     *
     * @since 0.73.4
     */
    private MayBeSlowNestedTest() {
    }

    @BeforeAll
    static void captureWarnings() {
        Logger.getLogger(MayBeSlow.class).addAppender(
            new WriterAppender(new PatternLayout("%m"), MayBeSlowNestedTest.SINK)
        );
    }

    @Nested
    final class Inner {

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3})
        @ExtendWith(MayBeSlow.class)
        void check(final int number) throws Exception {
            Thread.sleep(3_000L);
            final String warnings = MayBeSlowNestedTest.SINK.toString();
            Assertions.assertTrue(
                warnings.contains(String.format("check[#%d]", number)),
                String.format(
                    "No mention of check[#%d] in captured warnings: %s",
                    number,
                    warnings
                )
            );
        }
    }
}

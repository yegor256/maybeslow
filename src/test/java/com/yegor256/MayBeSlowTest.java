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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
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

    @Test
    void warnsAboutTestThreadState() throws Exception {
        final MayBeSlow extension = new MayBeSlow();
        final ExtensionContext ctx = new StubExtensionContext();
        final StringWriter sink = new StringWriter();
        final WriterAppender appender =
            new WriterAppender(new PatternLayout("%m%n"), sink);
        final Logger logger = Logger.getLogger(MayBeSlow.class.getName());
        logger.addAppender(appender);
        try {
            extension.beforeEach(ctx);
            Thread.sleep(4000L);
        } finally {
            extension.afterEach(ctx);
            logger.removeAppender(appender);
        }
        final long until = System.currentTimeMillis() + 5000L;
        String logged = sink.toString();
        while (!logged.contains("please wait") && System.currentTimeMillis() < until) {
            Thread.sleep(50L);
            logged = sink.toString();
        }
        Assertions.assertTrue(
            logged.contains("please wait")
                && logged.contains("We're waiting at")
                && !logged.contains("We're still running"),
            logged
        );
    }
}

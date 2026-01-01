/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2026 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.yegor256;

import com.jcabi.log.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * This class helps mark potentially slow test methods,
 * printing warning messages to the console.
 *
 * <p>An instance of this class is created by JUnit5 for
 * every unit-test method.</p>
 *
 * @since 0.1.0
 */
public final class MayBeSlow implements BeforeEachCallback, AfterEachCallback {

    /**
     * Watcher.
     */
    private Thread watch;

    @Override
    public void beforeEach(final ExtensionContext ctx) {
        final long start = System.currentTimeMillis();
        final String test = MayBeSlow.testOf(ctx);
        this.watch = new Thread(
            () -> {
                long cycle = 1L;
                while (true) {
                    try {
                        Thread.sleep(Math.min(2_000L * cycle, 60_000L));
                    } catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Logger.warn(
                        MayBeSlow.class,
                        "%s (%[ms]s), please wait...",
                        MayBeSlow.stateOf(this.watch, test),
                        System.currentTimeMillis() - start
                    );
                    ++cycle;
                }
            }
        );
        this.watch.start();
    }

    @Override
    public void afterEach(final ExtensionContext ctx) {
        this.watch.interrupt();
    }

    /**
     * Generate state of thread.
     * @param thread The thread
     * @param test Test name
     * @return Its status
     */
    private static String stateOf(final Thread thread, final String test) {
        final String sum;
        switch (thread.getState()) {
            case NEW:
                sum = String.format("We just started %s", test);
                break;
            case RUNNABLE:
                sum = String.format("We're still running %s", test);
                break;
            case BLOCKED:
                sum = String.format("We're blocked at %s", test);
                break;
            case WAITING:
            case TIMED_WAITING:
                sum = String.format("We're waiting at %s", test);
                break;
            case TERMINATED:
                sum = String.format("The test %s is terminated", test);
                break;
            default:
                sum = String.format("We're lost at %s", test);
                break;
        }
        return sum;
    }

    /**
     * Generate visually clean test method name.
     * @param ctx The context
     * @return Test method name
     */
    private static String testOf(final ExtensionContext ctx) {
        String test = ctx.getTestMethod().get().getName();
        final String[] parts = ctx.getUniqueId().split("/");
        if (parts.length > 3) {
            final Matcher mtc = Pattern.compile(
                "\\[test-template-invocation:#([0-9]+)]"
            ).matcher(parts[3]);
            if (mtc.find()) {
                final int index = Integer.parseInt(mtc.group(1));
                test = String.format("%s[#%d]", test, index);
            }
        }
        return test;
    }
}

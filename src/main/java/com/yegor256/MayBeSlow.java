/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256;

import com.jcabi.log.Logger;
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
     * When we started.
     */
    private final long start = System.currentTimeMillis();

    /**
     * Watcher.
     */
    private Thread watch;

    @Override
    public void beforeEach(final ExtensionContext ctx) {
        this.watch = new Thread(
            () -> {
                long cycle = 1L;
                while (true) {
                    try {
                        Thread.sleep(Math.min(5_000L * cycle, 60_000L));
                    } catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    Logger.warn(
                        MayBeSlow.class,
                        "We're still running the test (%[ms]s), please wait...",
                        System.currentTimeMillis() - this.start
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
}

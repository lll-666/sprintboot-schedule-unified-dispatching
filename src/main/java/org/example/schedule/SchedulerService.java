package org.example.schedule;

import java.util.concurrent.CompletableFuture;

/**
 * 该接口仅用于定时器的场景
 */
public interface SchedulerService {
    void handle();

    default void handleAsync() {
        CompletableFuture.runAsync(this::handle);
    }

    /**
     * 单位秒
     */
    int getRate();
}
package com.sample.base_project.common.utils.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

@Slf4j
public abstract class BaseMultiKeyQueue {
    private static final int MAX_QUEUE = 10_000;

    protected static final Map<String, LinkedBlockingQueue<BaseQueueData>> QUEUES = new HashMap<>();
    protected static final Map<String, LinkedBlockingQueue<DeadQueueData>> DEAD_QUEUES = new HashMap<>();

    @Getter
    protected static Map<String, Function<BaseQueueData, Void>> processingMap = new HashMap<>();

    protected static Map<String, Thread> threadMap = new HashMap<>();

    protected static void initQueue(String key, int maxQueue, Function<BaseQueueData, Void> processing) {
        QUEUES.put(key, new LinkedBlockingQueue<>(maxQueue));
        DEAD_QUEUES.put(key, new LinkedBlockingQueue<>(maxQueue));
        processingMap.put(key, processing);
        threadMap.put(key, new Thread(new QueueRunnable(QUEUES.get(key), DEAD_QUEUES.get(key), processingMap.get(key))));
        threadMap.get(key).start();
    }

    protected static void initQueue(String key, Function<BaseQueueData, Void> processing) {
        initQueue(key, MAX_QUEUE, processing);
    }

    public static <T extends BaseQueueData> boolean addQueue(String key, T data) {
        LinkedBlockingQueue<BaseQueueData> queue = QUEUES.get(key);
        boolean success = queue.add(data);
        if (!success) {
            // will wait 1 sec and retry once;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            success = queue.add(data);
        }
        return success;
    }

}

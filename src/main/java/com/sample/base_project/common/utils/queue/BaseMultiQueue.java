package com.sample.base_project.common.utils.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

@Slf4j
public abstract class BaseMultiQueue {
    private static final int MAX_THREAD = 10;
    private static final int MAX_QUEUE = 10_000;

    protected static final LinkedBlockingQueue<BaseQueueData>[] QUEUES = new LinkedBlockingQueue[MAX_THREAD];
    protected static final LinkedBlockingQueue<DeadQueueData>[] DEAD_QUEUES = new LinkedBlockingQueue[MAX_THREAD];

    @Getter
    protected static Function<BaseQueueData, Void>[] processings = new Function[MAX_THREAD];

    protected static Thread[] threads = new Thread[MAX_THREAD];

    protected static void initQueue(int index, Function<BaseQueueData, Void> processing) {
        QUEUES[index] = new LinkedBlockingQueue<>(MAX_QUEUE);
        DEAD_QUEUES[index] = new LinkedBlockingQueue<>(MAX_QUEUE);
        processings[index] = processing;
        threads[index] = new Thread(new QueueRunnable(QUEUES[index], DEAD_QUEUES[index], processings[index]));
        threads[index].start();
    }

    public static <T extends BaseQueueData> boolean addQueue(int index, T data) {
        boolean success = QUEUES[index].add(data);
        if (!success) {
            // will wait 1 sec and retry once;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            success = QUEUES[index].add(data);
        }
        return success;
    }

}

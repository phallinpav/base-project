package com.sample.base_project.common.utils.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

@Slf4j
public class QueueRunnable implements Runnable {
    private final LinkedBlockingQueue<BaseQueueData> queue;
    private final LinkedBlockingQueue<DeadQueueData> deadQueue;
    private final Function<BaseQueueData, Void> processing;

    public QueueRunnable(LinkedBlockingQueue<BaseQueueData> queue, LinkedBlockingQueue<DeadQueueData> deadQueue, Function<BaseQueueData, Void> processing) {
        this.queue = queue;
        this.deadQueue = deadQueue;
        this.processing = processing;
    }

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                BaseQueueData data;
                try {
                    data = queue.take();

                    try {
                        processing.apply(data);
                    } catch (Exception e) {
                        // SLEEP AND RETRY 3 TIMES
                        sleepRetry(3, data, e);
                    }
                } catch (InterruptedException e) {
                    // This case should not happen normally, but if you know when it can error please handle this error appropriately
                    log.error("fail to take queue", e);
                }
            }
            sleep(500);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // This case should not happen normally, but if you know when it can error please handle this error appropriately
            log.error("queue (fail sleep thread)", e);
        }
    }

    private void sleepRetry(long remainRetry, BaseQueueData data, Exception e) {
        if (remainRetry <= 0) {
            // FIXME: need to consider what will we do with deadQueue
            DeadQueueData deadData = DeadQueueData.builder()
                    .data(data)
                    .exception(e)
                    .build();
            if (!deadQueue.add(deadData)) {
                // FIXME: I really don't know what should be done if fail to add message here...
                //  please find solution
                log.error("fail to add to deadQueue after fail", e);
            }
            log.error("fail to process queue", e);
            return;
        }
        try {
            sleep(500);
            processing.apply(data);
        } catch (Exception e2) {
            sleepRetry(remainRetry - 1, data, e2);
        }
    }
}

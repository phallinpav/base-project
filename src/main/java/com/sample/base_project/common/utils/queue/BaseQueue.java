package com.sample.base_project.common.utils.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Slf4j
public abstract class BaseQueue {

    protected static final LinkedBlockingQueue<BaseQueueData> QUEUE = new LinkedBlockingQueue<>(10_000);
    protected static final LinkedBlockingQueue<DeadQueueData> DEAD_QUEUE = new LinkedBlockingQueue<>(10_000);

    @Getter
    protected static Function<BaseQueueData, Void> processing = (val) -> null;

    private final static Runnable mainRun = () -> {
        while (true) {
            while (!QUEUE.isEmpty()) {
                BaseQueueData data;
                try {
                    data = QUEUE.take();

                    try {
                        getProcessing().apply(data);
                    } catch (Exception e) {
                        // FIXME: need to consider what will we do with deadQueue
                        DeadQueueData deadData = DeadQueueData.builder()
                                .data(data)
                                .exception(e)
                                .build();
                        if (!DEAD_QUEUE.add(deadData)) {
                            // FIXME: I really don't know what should be done if fail to add message here...
                            //  please find solution
                            log.error("fail to add to deadQueue after fail", e);
                        }
                        log.error("fail to process queue", e);
                    }
                } catch (InterruptedException e) {
                    // This case should not happen normally, but if you know when it can error please handle this error appropriately
                    log.error("fail to take queue", e);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // This case should not happen normally, but if you know when it can error please handle this error appropriately
                log.error("queue (fail sleep thread)", e);
            }
        }
    };

    protected static Thread thread = new Thread(mainRun);

    public static <T extends BaseQueueData> boolean addQueue(T data) {
        boolean success = QUEUE.add(data);
        if (!success) {
            // will wait 1 sec and retry once;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            success = QUEUE.add(data);
        }
        return success;
    }

    @Scheduled(cron = "0 * * * * *") // every 1 minute
    public void threadCheck() {
        if (thread.isAlive()) {
            System.out.println("Thread is alive");
        } else {
            System.out.println("Thread is not alive");
            thread = new Thread(mainRun);
            thread.start();
            System.out.println("Thread is restarting");
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    public void deadQueueCheck() {
        if (!DEAD_QUEUE.isEmpty()) {
            log.info(String.format("There are %d error in queue", DEAD_QUEUE.size()));
            AtomicInteger i = new AtomicInteger();
            DEAD_QUEUE.forEach((val) -> {
                String message = String.format("dead queue(%d): %s", i.getAndIncrement(), val.toString());
                log.info(message);
                // TODO: need to handle what we need to do to dead queue
            });
        }
    }
}

package com.sample.base_project.common.utils.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class HealthCheckQueue extends BaseMultiQueue {
    @Scheduled(cron = "0 * * * * *") // every 1 minute
    public void threadCheck() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null) {
                Thread thread = threads[i];
                if (thread.isAlive()) {
                    System.out.printf("Thread[%d] is alive\n", i);
                } else {
                    System.out.printf("Thread[%d] is not alive\n", i);
                    thread = new Thread(new QueueRunnable(QUEUES[i], DEAD_QUEUES[i], processings[i]));
                    thread.start();
                    System.out.printf("Thread[%d] is restarting\n", i);
                }
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    public void deadQueueCheck() {
        for (int i = 0; i < threads.length; i++) {
            if (DEAD_QUEUES[i] != null) {
                var deadQueue = DEAD_QUEUES[i];
                if (!deadQueue.isEmpty()) {
                    log.info(String.format("There are %d error in queue[%d]", deadQueue.size(), i));
                    AtomicInteger qIdx = new AtomicInteger();
                    for (var val : deadQueue) {
                        String message = String.format("dead queue[%d] error(%d): %s", i, qIdx.getAndIncrement(), val.toString());
                        log.info(message);
                        // TODO: need to handle what we need to do to dead queue
                    }
                }
            }
        }
    }
}

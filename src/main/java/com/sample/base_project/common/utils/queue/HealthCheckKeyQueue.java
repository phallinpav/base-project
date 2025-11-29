package com.sample.base_project.common.utils.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class HealthCheckKeyQueue extends BaseMultiKeyQueue {
    @Scheduled(cron = "0 * * * * *") // every 1 minute
    public void threadCheck() {
        for (var set : threadMap.entrySet()) {
            String key = set.getKey();
            Thread thread = set.getValue();
            if (thread != null) {
                if (thread.isAlive()) {
                    System.out.printf("Thread[%s] is alive\n", key);
                } else {
                    System.out.printf("Thread[%s] is not alive\n", key);
                    thread = new Thread(new QueueRunnable(QUEUES.get(key), DEAD_QUEUES.get(key), processingMap.get(key)));
                    thread.start();
                    System.out.printf("Thread[%s] is restarting\n", key);
                }
            }
        }
    }

    @Scheduled(cron = "0 */5 * * * *") // every 5 minutes
    public void deadQueueCheck() {
        for (var set : threadMap.entrySet()) {
            String key = set.getKey();
            Thread thread = set.getValue();
            var deadQueue = DEAD_QUEUES.get(key);
            if (!deadQueue.isEmpty()) {
                log.info(String.format("There are %d error in queue[%s]", deadQueue.size(), key));
                AtomicInteger qIdx = new AtomicInteger();
                for (var val : deadQueue) {
                    String message = String.format("dead queue[%s] error(%d): %s", key, qIdx.getAndIncrement(), val.toString());
                    log.info(message);
                    // TODO: need to handle what we need to do to dead queue
                }
            }
        }
    }
}

package com.sample.base_project.common.utils.queue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeadQueueData implements BaseQueueData {
    private BaseQueueData data;
    private Exception exception;
}

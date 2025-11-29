package com.sample.base_project.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;

public interface BaseEntity {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    default Long getUuid() {
        return 0L;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    default String get_id() {
        return String.valueOf(getUuid());
    }
}

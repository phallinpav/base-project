package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class GetBasePageParam<T extends BaseEntity> extends GetBaseListParam<T> implements Serializable {

    public GetBasePageParam<T> nextPage() {
        if (this.page == null) this.page = 1;
        this.page += 1;
        return this;
    }
}

package com.sample.base_project.common.base.view;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class DeleteBaseParam<ID> extends AuthParam implements Serializable {
    @NotNull
    protected ID uuid;

    public static <ID> DeleteBaseParam<ID> of(ID uuid) {
        return DeleteBaseParam.<ID>builder()
                .uuid(uuid)
                .build();
    }
}

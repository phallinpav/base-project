package com.sample.base_project.common.base.view;

import com.sample.base_project.common.utils.filter.SpecificationComposition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class GetBaseOneParam<T> extends AuthParam implements Serializable {
    protected boolean nullable;
    protected boolean mapRef;
    protected Set<String> mapKeyInclude;
    protected Set<String> mapKeyExclude;

    public abstract SpecificationComposition<T> toSpecCom();

    public Specification<T> toSpec() {
        return toSpecCom().toSpec();
    }

    public Query toQuery() {
        return toSpecCom().toQuery();
    }
}

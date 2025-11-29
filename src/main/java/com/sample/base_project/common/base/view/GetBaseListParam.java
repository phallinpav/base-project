package com.sample.base_project.common.base.view;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.utils.filter.BaseSpecification;
import com.sample.base_project.common.utils.filter.SpecificationComposition;
import com.sample.base_project.common.utils.filter.SpecificationUtils;
import com.sample.base_project.common.utils.paging.BasePageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class GetBaseListParam<T extends BaseEntity> extends BasePageParam<T> implements Serializable {

    protected Collection<Long> uuids;

    protected Timestamp createdDateFrom;
    protected Timestamp createdDateTo;

    protected Timestamp updatedDateFrom;
    protected Timestamp updatedDateTo;

    protected boolean mapRef;
    protected Set<String> mapKeyInclude;
    protected Set<String> mapKeyExclude;

    public static <T extends BaseEntity> GetBaseListParam<T> of(Collection<Long> uuids, boolean mapRef) {
        var param = new GetBaseListParam<T>();
        param.setUuids(uuids);
        param.setMapRef(mapRef);
        return param;
    }

    public static <T extends BaseEntity> GetBaseListParam<T> of(Collection<Long> uuids,
                                                                Set<String> mapKeyInclude,
                                                                Set<String> mapKeyExclude) {
        var param = new GetBaseListParam<T>();
        param.setUuids(uuids);
        param.setMapRef(true);
        param.setMapKeyInclude(mapKeyInclude);
        param.setMapKeyExclude(mapKeyExclude);
        return param;
    }

    public static <T extends BaseEntity> GetBaseListParam<T> of(Collection<Long> uuids,
                                                                boolean mapRef,
                                                                Set<String> mapKeyInclude,
                                                                Set<String> mapKeyExclude) {
        var param = new GetBaseListParam<T>();
        param.setUuids(uuids);
        param.setMapRef(mapRef);
        param.setMapKeyInclude(mapKeyInclude);
        param.setMapKeyExclude(mapKeyExclude);
        return param;
    }

    public static <T extends BaseEntity> GetBaseListParam<T> of(Collection<Long> uuids) {
        return of(uuids, false);
    }

    public SpecificationComposition<T> toSpecCom() {
        List<BaseSpecification<T>> list = new ArrayList<>();

        if (uuids != null && !uuids.isEmpty()) {
            list.add(SpecificationUtils.compareIn("uuid", uuids));
        }

        if (createdDateFrom != null) {
            list.add(SpecificationUtils.compareGreaterThanEqual("createdAt", createdDateFrom));
        }
        if (createdDateTo != null) {
            list.add(SpecificationUtils.compareLessThanEqual("createdAt", createdDateTo));
        }
        if (updatedDateFrom != null) {
            list.add(SpecificationUtils.compareGreaterThanEqual("updatedAt", updatedDateFrom));
        }
        if (updatedDateTo != null) {
            list.add(SpecificationUtils.compareLessThanEqual("updatedAt", updatedDateTo));
        }

        return SpecificationComposition.with(list);
    }

    public Specification<T> toSpec() {
        return toSpecCom().toSpec();
    }

    public Query toQuery() {
        return toSpecCom().toQuery();
    }
}

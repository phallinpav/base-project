package com.sample.base_project.common.utils.filter;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SpecificationComposition<T> implements Serializable {
    private List<BaseSpecification<T>> baseSpecifications;
    private Boolean and;
    private SpecificationComposition<T> otherSpec;
    private Boolean andSpec;
    private SpecificationComposition<T> otherSpec2;
    private Boolean andSpec2;

    public Query toQuery() {
        return new Query(toCriteria());
    }

    public Criteria toCriteria() {
        Criteria criteria = new Criteria();

        if (and == null) and = true;
        if (andSpec == null) andSpec = true;

        if (baseSpecifications == null) {
            baseSpecifications = new ArrayList<>();
        }

        List<Criteria> criteriaList1 = new ArrayList<>();
        for (BaseSpecification<T> baseSpec : baseSpecifications) {
            criteriaList1.add(baseSpec.toMongoCriteria());
        }

        if (!criteriaList1.isEmpty()) {
            if (and) {
                criteria.andOperator(criteriaList1);
            } else {
                criteria.orOperator(criteriaList1);
            }
        }

        if (otherSpec != null) {
            if (andSpec) {
                criteria = new Criteria().andOperator(criteria, otherSpec.toCriteria());
            } else {
                criteria = new Criteria().orOperator(criteria, otherSpec.toCriteria());
            }
        }
        if (otherSpec2 != null) {
            if (andSpec2) {
                criteria = new Criteria().andOperator(criteria, otherSpec2.toCriteria());
            } else {
                criteria = new Criteria().orOperator(criteria, otherSpec2.toCriteria());
            }
        }
        return criteria;
    }

    public Specification<T> toSpec() {
        if (and == null) and = true;
        if (andSpec == null) andSpec = true;

        Specification<T> spec = null;
        if (baseSpecifications == null) {
            baseSpecifications = new ArrayList<>();
        }

        for (BaseSpecification<T> baseSpec : baseSpecifications) {
            if (spec == null) {
                spec = Specification.where(baseSpec);
            } else if (and) {
                spec = spec.and(baseSpec);
            } else {
                spec = spec.or(baseSpec);
            }
        }
        if (otherSpec != null) {
            if (spec == null) {
                spec = otherSpec.toSpec();
            } else if (andSpec) {
                spec = spec.and(otherSpec.toSpec());
            } else {
                spec = spec.or(otherSpec.toSpec());
            }
        }
        if (otherSpec2 != null) {
            if (spec == null) {
                spec = otherSpec2.toSpec();
            } else if (andSpec2) {
                spec = spec.and(otherSpec2.toSpec());
            } else {
                spec = spec.or(otherSpec2.toSpec());
            }
        }
        return spec;
    }

    public static <T> SpecificationComposition<T> with(SpecificationComposition<T> spec1, boolean and, SpecificationComposition<T> spec2) {
        SpecificationComposition<T> spec = new SpecificationComposition<>();
        spec.setOtherSpec(spec1);
        spec.setAndSpec(and);
        spec.setOtherSpec2(spec2);
        spec.setAndSpec2(and);
        return spec;
    }

    public static <T> SpecificationComposition<T> with(List<BaseSpecification<T>> baseSpec, SpecificationComposition<T> otherSpec) {
        return with(true, baseSpec, true, otherSpec);
    }

    public static <T> SpecificationComposition<T> with(List<BaseSpecification<T>> baseSpec, boolean andSpec, SpecificationComposition<T> otherSpec) {
        return with(true, baseSpec, andSpec, otherSpec);
    }

    public static <T> SpecificationComposition<T> with(boolean and, List<BaseSpecification<T>> baseSpec, boolean andSpec, SpecificationComposition<T> otherSpec) {
        SpecificationComposition<T> spec = with(and, baseSpec);
        spec.setOtherSpec(otherSpec);
        spec.setAndSpec(andSpec);
        return spec;
    }

    public static <T> SpecificationComposition<T> with(List<BaseSpecification<T>> baseSpecs) {
        return with(true, baseSpecs);
    }

    public static <T> SpecificationComposition<T> with(boolean baseListAnd, List<BaseSpecification<T>> baseSpecs, boolean andSpec, List<BaseSpecification<T>> otherSpecs, boolean otherListAnd) {
        SpecificationComposition<T> specCom = with(baseListAnd, baseSpecs);
        specCom.setOtherSpec(with(otherListAnd, otherSpecs));
        specCom.setAndSpec(andSpec);
        return specCom;
    }

    public static <T> SpecificationComposition<T> with(List<BaseSpecification<T>> baseSpecs, List<BaseSpecification<T>> otherSpecs) {
        return with(baseSpecs, true, otherSpecs);
    }

    public static <T> SpecificationComposition<T> with(List<BaseSpecification<T>> baseSpecs, boolean andSpec, List<BaseSpecification<T>> otherSpecs) {
        SpecificationComposition<T> specCom = with(baseSpecs);
        specCom.setOtherSpec(with(otherSpecs));
        specCom.setAndSpec(andSpec);
        return specCom;
    }

    @SafeVarargs
    public static <T> SpecificationComposition<T> with(BaseSpecification<T>... baseSpecs) {
        return with(true, baseSpecs);
    }

    @SafeVarargs
    public static <T> SpecificationComposition<T> with(boolean and, BaseSpecification<T>... baseSpecs) {
        return with(and, List.of(baseSpecs));
    }

    @SafeVarargs
    public static <T> SpecificationComposition<T> with(boolean and, SpecificationComposition<T>... specs) {
        SpecificationComposition<T> firstSpec = null;
        SpecificationComposition<T> resultSpec = null;
        for (var spec : specs) {
            if (spec.getBaseSpecifications() == null || spec.getBaseSpecifications().isEmpty()) {
                continue;
            }
            if (resultSpec == null) {
                resultSpec = spec;
                firstSpec = spec;
                continue;
            }
            resultSpec.setOtherSpec(spec);
            resultSpec.setAndSpec(and);
            resultSpec = spec;
        }
        // possible to return null
        return firstSpec;
    }

    public static <T> SpecificationComposition<T> with(boolean and, List<BaseSpecification<T>> baseSpecs) {
        SpecificationComposition<T> specCom = new SpecificationComposition<>();
        specCom.setAnd(and);
        specCom.setBaseSpecifications(baseSpecs);
        return specCom;
    }
}

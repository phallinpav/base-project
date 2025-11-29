package com.sample.base_project.common.utils.filter;

import com.sample.base_project.common.utils.common.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.Bindable;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.PluralAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collection;

@RequiredArgsConstructor
@Data
public class BaseSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root,
                                 @NonNull CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder builder) {
        Object value = criteria.getValue();
        if (criteria.getGroupByKeys() != null && criteria.getGroupByEntity() != null && criteria.getGroupByJoinType() != null) {
            Join<Object, Object> join = root.join(criteria.getGroupByEntity(), criteria.getGroupByJoinType());
            for (String groupByKey : criteria.getGroupByKeys()) {
                query.groupBy(join.get(groupByKey));
            }
        }
        if (criteria.isDistinct()) {
            query.distinct(true);
        }


        if (criteria.getJoinEntityCondition() != null && criteria.getJoinSearchCondition() != null) {
            Root<?> joinRoot = query.from(criteria.getJoinEntityCondition().joinClass());
            Predicate joinPre = doOperation(builder,
                    criteria.getJoinEntityCondition().operator(),
                    root.get(criteria.getJoinEntityCondition().rootColumn()),
                    joinRoot.get(criteria.getJoinEntityCondition().joinColumn()));

            Predicate joinSearchPre = doOperation(builder, criteria.getJoinSearchCondition().operator(),
                    joinRoot.get(criteria.getJoinSearchCondition().key()), criteria.getJoinSearchCondition().value());
            if (criteria.getIfNullIsTrue() != null) {
                if (criteria.getIfNullIsTrue()) {
                    joinSearchPre = builder.or(joinSearchPre, builder.isNull(joinSearchPre));
                    joinPre = builder.or(joinPre, builder.isNull(joinPre));
                } else {
                    joinSearchPre = builder.or(joinSearchPre, builder.isNotNull(joinSearchPre));
                    joinPre = builder.or(joinPre, builder.isNotNull(joinPre));
                }
            }
            return builder.and(joinSearchPre, joinPre);
        }

        return doOperation(builder, criteria.getOperation(), getPath(root), value);
    }

    // FIXME: a bit duplicated and weird
    private Predicate doOperation(CriteriaBuilder builder, SearchCriteria.Operator operator, Path path, Path value) {
        return switch (operator) {
            case EQ -> builder.equal(path, value);
            case NOT_EQ -> builder.notEqual(path, value);
            case IN -> path.in(value);
            case NOT_IN -> path.in(value).not();
            case LIKE -> builder.like(path.as(String.class), String.valueOf(value));
            case ILIKE -> builder.like(builder.upper(path.as(String.class)), builder.upper(value.as(String.class)));
            case NOT_LIKE -> builder.notLike(path.as(String.class), value.as(String.class));
            case NOT_ILIKE -> builder.notLike(builder.upper(path.as(String.class)), builder.upper(value.as(String.class)));
            case IS_NULL -> builder.isNull(path);
            case IS_NOT_NULL -> builder.isNotNull(path);
            case LT -> builder.lessThan(path, value);
            case LTE -> builder.lessThanOrEqualTo(path, value);
            case GT -> builder.greaterThan(path, value);
            case GTE -> builder.greaterThanOrEqualTo(path, value);
        };
    }

    private Predicate doOperation(CriteriaBuilder builder, SearchCriteria.Operator operator, Path path, Object value) {
        return switch (operator) {
            case EQ -> builder.equal(path, value);
            case NOT_EQ -> builder.notEqual(path, value);
            case IN -> path.in((Collection<?>) value);
            case NOT_IN -> path.in((Collection<?>) value).not();
            case LIKE -> builder.like(path.as(String.class), String.valueOf(value));
            case ILIKE -> builder.like(builder.upper(path.as(String.class)), String.valueOf(value).toUpperCase());
            case NOT_LIKE -> builder.notLike(path.as(String.class), String.valueOf(value));
            case NOT_ILIKE -> builder.notLike(builder.upper(path.as(String.class)), String.valueOf(value).toUpperCase());
            case IS_NULL -> builder.isNull(path);
            case IS_NOT_NULL -> builder.isNotNull(path);
            case LT -> builder.lessThan(path, (Comparable) value);
            case LTE -> builder.lessThanOrEqualTo(path, (Comparable) value);
            case GT -> builder.greaterThan(path, (Comparable) value);
            case GTE -> builder.greaterThanOrEqualTo(path, (Comparable) value);
        };
    }

    @SuppressWarnings("unchecked")
    private <Y> Path<Y> castPath(Path<?> path) {
        return (Path<Y>) path;
    }

    private <Y> Path<Y> getPath(Root<T> root) {
        String key = criteria.getKey();
        if (StringUtils.isNotBlank(criteria.getKeyPrefix())) {
            key = criteria.getKeyPrefix() + "." + key;
        }

        if (criteria.getJoinEntity() != null) {
            if (criteria.getJoinType() != null) {
                return root.join(criteria.getJoinEntity(), criteria.getJoinType()).get(key);
            } else {
                return root.join(criteria.getJoinEntity()).get(key);
            }
        }

        String[] splitKey = key.split("\\.");
        Path<?> path = root;

        for (String part : splitKey) {
            Bindable<?> model = path.getModel();
            if (model instanceof EntityType<?> entityType) {
                Attribute<?, ?> attribute = entityType.getAttribute(part);
                switch (attribute.getPersistentAttributeType()) {
                    case ONE_TO_MANY, MANY_TO_MANY, ELEMENT_COLLECTION ->
                            path = path instanceof Root<?> ? ((Root<?>) path).join(part, JoinType.LEFT) : ((Join<?, ?>) path).join(part, JoinType.LEFT);
                    case MANY_TO_ONE, ONE_TO_ONE, BASIC ->
                            path = path.get(part);
                    default ->
                            throw new IllegalArgumentException("Cannot join to attribute of unknown type: " + part);
                }
            } else if (model instanceof SingularAttribute<?, ?> || model instanceof PluralAttribute<?, ?, ?>) {
                path = path.get(part);
            } else {
                throw new IllegalArgumentException("Cannot join to attribute of unknown type: " + part);
            }
        }
        return castPath(path);
    }

    public Criteria toMongoCriteria() {
        Criteria criteria = new Criteria();
        Object value = this.criteria.getValue();
        switch (this.criteria.getOperation()) {
            case EQ -> criteria.and(this.criteria.getKey()).is(value);
            case NOT_EQ -> criteria.and(this.criteria.getKey()).ne(value);
            case IN -> criteria.and(this.criteria.getKey()).in((Collection<?>) value);
            case NOT_IN -> criteria.and(this.criteria.getKey()).nin((Collection<?>) value);
            case LIKE -> criteria.and(this.criteria.getKey()).regex(String.valueOf(value));
            case ILIKE -> criteria.and(this.criteria.getKey()).regex(String.valueOf(value), "i");
            case NOT_LIKE -> criteria.and(this.criteria.getKey()).not().regex(String.valueOf(value));
            case NOT_ILIKE -> criteria.and(this.criteria.getKey()).not().regex(String.valueOf(value), "i");
            case IS_NULL -> criteria.and(this.criteria.getKey()).isNull();
            case IS_NOT_NULL -> criteria.and(this.criteria.getKey()).ne(null);
            case LT -> criteria.and(this.criteria.getKey()).lt(value);
            case LTE -> criteria.and(this.criteria.getKey()).lte(value);
            case GT -> criteria.and(this.criteria.getKey()).gt(value);
            case GTE -> criteria.and(this.criteria.getKey()).gte(value);
        };
        return criteria;
    }
}

package com.sample.base_project.common.utils.filter;

import com.sample.base_project.common.utils.common.ApplicationContextProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CompoundSelection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CustomRepository {
    default <T, U> Page<T> getCustomGroupBy(@NonNull Class<T> responseClazz,
                                            @NonNull Class<U> specClazz,
                                            @Nullable Specification<U> spec,
                                            @NonNull Pageable pageable,
                                            @NonNull List<Function<Root<U>, Expression<?>>> groupByFunctions,
                                            @NonNull BiFunction<CriteriaBuilder, Root<U>, CompoundSelection<T>> compoundSelection,
                                            @Nullable TriFunction<CriteriaBuilder, Root<U>, String, Expression<?>> customSort) {

        EntityManager entityManager = ApplicationContextProvider.getBean(EntityManager.class);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(responseClazz);
        Root<U> root = query.from(specClazz);

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, builder);
            query.where(predicate);
        }

        List<Expression<?>> groupBys = new ArrayList<>();
        for (var val : groupByFunctions) {
            groupBys.add(val.apply(root));
        }

        query.groupBy(groupBys);
        query.select(compoundSelection.apply(builder, root));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order sortOrder : pageable.getSort()) {
                String property = sortOrder.getProperty();
                Expression<?> expression;
                if (customSort == null) {
                    expression = null;
                } else {
                    expression = customSort.apply(builder, root, property);
                }
                if (expression == null) {
                    if (property.contains(".")) {
                        String[] split = property.split("\\.");
                        Path<?> path = null;
                        for (var s : split) {
                            path = Objects.requireNonNullElse(path, root).get(s);
                        }
                        expression = path;
                    } else {
                        expression = root.get(property);
                    }
                }

                if (sortOrder.isAscending()) {
                    orders.add(builder.asc(expression));
                } else {
                    orders.add(builder.desc(expression));
                }
            }
            query.orderBy(orders);
        }

        TypedQuery<T> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<T> results = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<U> countRoot = countQuery.from(specClazz);
        if (groupByFunctions.size() > 1) {
            Expression<String> concatedExpression = null;
            for (var val : groupByFunctions) {
                Expression<String> e = val.apply(countRoot).as(String.class);
                if (concatedExpression == null) {
                    concatedExpression = e;
                } else {
                    concatedExpression = builder.concat(builder.concat(concatedExpression, "_"), e);
                }
            }
            countQuery.select(builder.countDistinct(concatedExpression));
        } else {
            countQuery.select(builder.countDistinct(groupByFunctions.get(0).apply(countRoot)));
        }

        if (spec != null) {
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, builder);
            countQuery.where(countPredicate);
        }

        Long totalRecords = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, totalRecords);
    }


    default <T, U> Page<T> getCustomGroupBy(@NonNull Class<T> responseClazz,
                                            @NonNull Class<U> specClazz,
                                            @Nullable Specification<U> spec,
                                            @NonNull Pageable pageable,
                                            @NonNull List<Function<Root<U>, Expression<?>>> groupByFunctions,
                                            @NonNull BiFunction<CriteriaBuilder, Root<U>, CompoundSelection<T>> compoundSelection) {
        return getCustomGroupBy(responseClazz, specClazz, spec, pageable, groupByFunctions, compoundSelection, null);
    }

    default <T, U> Page<T> getCustomGroupBy(@NonNull Class<T> responseClazz,
                                            @NonNull Class<U> specClazz,
                                            @Nullable Specification<U> spec,
                                            @NonNull Pageable pageable,
                                            @NonNull Function<Root<U>, Expression<?>> groupByFunction,
                                            @NonNull BiFunction<CriteriaBuilder, Root<U>, CompoundSelection<T>> compoundSelection,
                                            @Nullable TriFunction<CriteriaBuilder, Root<U>, String, Expression<?>> customSort) {
        return getCustomGroupBy(responseClazz, specClazz, spec, pageable, List.of(groupByFunction), compoundSelection, customSort);
    }

    default <T, U> Page<T> getCustomGroupBy(@NonNull Class<T> responseClazz,
                                            @NonNull Class<U> specClazz,
                                            @Nullable Specification<U> spec,
                                            @NonNull Pageable pageable,
                                            @NonNull Function<Root<U>, Expression<?>> groupByFunction,
                                            @NonNull BiFunction<CriteriaBuilder, Root<U>, CompoundSelection<T>> compoundSelection) {
        return getCustomGroupBy(responseClazz, specClazz, spec, pageable, groupByFunction, compoundSelection, null);
    }

}

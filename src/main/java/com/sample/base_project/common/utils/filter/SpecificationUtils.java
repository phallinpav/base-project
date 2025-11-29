package com.sample.base_project.common.utils.filter;

public class SpecificationUtils {

    public static <T> BaseSpecification<T> search(SearchCriteria searchCriteria) {
        return new BaseSpecification<>(searchCriteria);
    }

    public static <T> BaseSpecification<T> compare(String field, SearchCriteria.Operator operator, Object value) {
        return new BaseSpecification<>(new SearchCriteria(field, operator, value));
    }

    public static <T> BaseSpecification<T> compareEqual(String field, Object value) {
        return compare(field, SearchCriteria.Operator.EQ, value);
    }

    public static <T> BaseSpecification<T> compareNotEqual(String field, Object value) {
        return compare(field, SearchCriteria.Operator.NOT_EQ, value);
    }

    public static <T> BaseSpecification<T> compareIn(String field, Object value) {
        return compare(field, SearchCriteria.Operator.IN, value);
    }

    public static <T> BaseSpecification<T> compareNotIn(String field, Object value) {
        return compare(field, SearchCriteria.Operator.NOT_IN, value);
    }

    public static <T> BaseSpecification<T> compareLike(String field, Object value, boolean ignoreCase) {
        return compare(field, ignoreCase ? SearchCriteria.Operator.ILIKE : SearchCriteria.Operator.LIKE, value);
    }

    public static <T> BaseSpecification<T> compareLikeAll(String field, Object value, boolean ignoreCase) {
        return compareLike(field, String.format("%%%s%%", value), ignoreCase);
    }

    public static <T> BaseSpecification<T> compareNotLike(String field, Object value, boolean ignoreCase) {
        return compare(field, ignoreCase ? SearchCriteria.Operator.NOT_ILIKE : SearchCriteria.Operator.NOT_LIKE, value);
    }

    public static <T> BaseSpecification<T> compareNotLikeAll(String field, Object value, boolean ignoreCase) {
        return compareNotLike(field, String.format("%%%s%%", value), ignoreCase);
    }

    public static <T> BaseSpecification<T> isNull(String field) {
        return compare(field, SearchCriteria.Operator.IS_NULL, null);
    }

    public static <T> BaseSpecification<T> isNotNull(String field) {
        return compare(field, SearchCriteria.Operator.IS_NOT_NULL, null);
    }

    public static <T> BaseSpecification<T> compareLessThan(String field, Object value) {
        return compare(field, SearchCriteria.Operator.LT, value);
    }
    public static <T> BaseSpecification<T> compareLessThanEqual(String field, Object value) {
        return compare(field, SearchCriteria.Operator.LTE, value);
    }
    public static <T> BaseSpecification<T> compareGreaterThan(String field, Object value) {
        return compare(field, SearchCriteria.Operator.GT, value);
    }
    public static <T> BaseSpecification<T> compareGreaterThanEqual(String field, Object value) {
        return compare(field, SearchCriteria.Operator.GTE, value);
    }

    public static <T> BaseSpecification<T> joinSearch(SearchCriteria.JoinEntityCondition joinEntityCondition, SearchCriteria.JoinSearchCondition joinSearchCondition) {
        return new BaseSpecification<>(new SearchCriteria(joinEntityCondition, joinSearchCondition));
    }

    public static <T> BaseSpecification<T> joinCompare(Class<?> joinClass, String rootColumn, String joinColumn, SearchCriteria.Operator joinOperator, String field, SearchCriteria.Operator operator, Object value) {
        SearchCriteria.JoinSearchCondition joinSearchCondition = new SearchCriteria.JoinSearchCondition(field, operator, value);
        SearchCriteria.JoinEntityCondition joinEntityCondition = new SearchCriteria.JoinEntityCondition(joinClass, rootColumn, joinColumn, joinOperator);
        return joinSearch(joinEntityCondition, joinSearchCondition);
    }

    public static <T> BaseSpecification<T> joinSearchEqual(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.EQ, value);
    }

    public static <T> BaseSpecification<T> joinSearchNotEqual(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.NOT_EQ, value);
    }

    public static <T> BaseSpecification<T> joinSearchIn(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.IN, value);
    }

    public static <T> BaseSpecification<T> joinSearchNotIn(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.NOT_IN, value);
    }

    public static <T> BaseSpecification<T> joinSearchLike(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value, boolean ignoreCase) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, ignoreCase ? SearchCriteria.Operator.ILIKE : SearchCriteria.Operator.LIKE, value);
    }

    public static <T> BaseSpecification<T> joinSearchLikeAll(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value, boolean ignoreCase) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, ignoreCase ? SearchCriteria.Operator.ILIKE : SearchCriteria.Operator.LIKE, String.format("%%%s%%", value));
    }

    public static <T> BaseSpecification<T> joinSearchNotLike(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value, boolean ignoreCase) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, ignoreCase ? SearchCriteria.Operator.NOT_ILIKE : SearchCriteria.Operator.NOT_LIKE, value);
    }

    public static <T> BaseSpecification<T> joinSearchNotLikeAll(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value, boolean ignoreCase) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, ignoreCase ? SearchCriteria.Operator.NOT_ILIKE : SearchCriteria.Operator.NOT_LIKE, String.format("%%%s%%", value));
    }

    public static <T> BaseSpecification<T> joinIsNull(Class<?> joinClass, String rootColumn, String joinColumn, String field) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.IS_NULL, null);
    }

    public static <T> BaseSpecification<T> joinIsNotNull(Class<?> joinClass, String rootColumn, String joinColumn, String field) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.IS_NOT_NULL, null);
    }

    public static <T> BaseSpecification<T> joinLessThan(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.LT, value);
    }

    public static <T> BaseSpecification<T> joinLessThanEqual(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.LTE, value);
    }

    public static <T> BaseSpecification<T> joinGreaterThan(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.GT, value);
    }

    public static <T> BaseSpecification<T> joinGreaterThanEqual(Class<?> joinClass, String rootColumn, String joinColumn, String field, Object value) {
        return joinCompare(joinClass, rootColumn, joinColumn, SearchCriteria.Operator.EQ, field, SearchCriteria.Operator.GTE, value);
    }

}

package com.sample.base_project.common.utils.filter;

import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchCriteria implements Serializable {
    private String keyPrefix;
    private String key;
    private Operator operation;
    private Boolean ifNullIsTrue;
    private Object value;
    private String joinEntity;
    private JoinType joinType;
    private boolean distinct;

    // FIXME: NOT YET WORKING PROPERLY
    private List<String> groupByKeys;
    private String groupByEntity;
    private JoinType groupByJoinType;

    private JoinEntityCondition joinEntityCondition;
    private JoinSearchCondition joinSearchCondition;

    public SearchCriteria(String key, Operator operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(JoinEntityCondition joinEntityCondition, JoinSearchCondition joinSearchCondition) {
        this.joinEntityCondition = joinEntityCondition;
        this.joinSearchCondition = joinSearchCondition;
        this.distinct = true;
    }

    public record JoinEntityCondition(Class<?> joinClass, String rootColumn, String joinColumn, Operator operator) {
    }

    public record JoinSearchCondition(String key, Operator operator, Object value) {
    }

    public enum Operator {
        EQ,
        NOT_EQ,
        IN,
        NOT_IN,
        LIKE,
        ILIKE,
        NOT_LIKE,
        NOT_ILIKE,
        IS_NULL,
        IS_NOT_NULL,
        LT,
        LTE,
        GT,
        GTE,

    }
}

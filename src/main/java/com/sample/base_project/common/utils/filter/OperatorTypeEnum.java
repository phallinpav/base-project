package com.sample.base_project.common.utils.filter;

public enum OperatorTypeEnum {
    IS("is"),
    IS_NOT("is_not"),
    CONTAIN("contain"),
    NOT_CONTAIN("not_contain"),
    IS_EMPTY("is_empty"),
    IS_NOT_EMPTY("is_not_empty"),
    GREATER_THAN("greater_than"),
    GREATER_THAN_EQUAL("greater_than_equal"),
    LESS_THAN("less_than"),
    LESS_THAN_EQUAL("less_than_equal");

    private final String value;
    OperatorTypeEnum(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public String toMessageValidationValue() {
        // message: must be ...
        return switch (this) {
            case IS -> "equal to";
            case IS_NOT -> "not equal to";
            case CONTAIN -> "containing";
            case NOT_CONTAIN -> "not containing";
            case GREATER_THAN -> "greater than";
            case GREATER_THAN_EQUAL -> "greater than or equal to";
            case LESS_THAN -> "less than";
            case LESS_THAN_EQUAL -> "less than or equal to";
            case IS_EMPTY -> "empty";
            case IS_NOT_EMPTY -> "not empty";
        };
    }

    public OperatorTypeEnum toOpposite() {
        return switch (this) {
            case IS -> IS_NOT;
            case IS_NOT -> IS;
            case CONTAIN -> NOT_CONTAIN;
            case NOT_CONTAIN -> CONTAIN;
            case IS_EMPTY -> IS_NOT_EMPTY;
            case IS_NOT_EMPTY -> IS_EMPTY;
            case GREATER_THAN -> LESS_THAN_EQUAL;
            case GREATER_THAN_EQUAL -> LESS_THAN;
            case LESS_THAN -> GREATER_THAN_EQUAL;
            case LESS_THAN_EQUAL -> GREATER_THAN;
        };
    }
}

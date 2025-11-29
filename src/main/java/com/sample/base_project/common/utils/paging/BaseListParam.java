package com.sample.base_project.common.utils.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.view.BaseAuthParam;
import com.sample.base_project.common.utils.common.StringUtils;
import com.sample.base_project.common.utils.filter.OperatorTypeEnum;
import com.sample.base_project.common.utils.validation.common.CanHaveOnlyOne;
import com.sample.base_project.common.utils.validation.common.CollectionSameSize;
import com.sample.base_project.common.utils.validation.common.Condition;
import com.sample.base_project.common.utils.validation.common.FixedValue;
import com.sample.base_project.common.utils.validation.common.MultipleValidations;
import com.sample.base_project.common.utils.validation.common.NullOrNotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@MultipleValidations(canHaveOnlyOnes = {
        @CanHaveOnlyOne(fields = {"field", "fields"}),
        @CanHaveOnlyOne(fields = {"direction", "directions"})
})
@CollectionSameSize(fields = {"fields", "directions"}, ifConditions = @Condition(field = "directions", operator = OperatorTypeEnum.IS_NOT_EMPTY))
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class BaseListParam<T extends BaseEntity> extends BaseAuthParam<T> implements Serializable {
    @FixedValue(classes = Direction.class)
    protected String direction;

    @NullOrNotBlank
    protected String field;

    @Size(min = 1, max = 10)
    protected List<String> fields;

    @Size(min = 1, max = 10)
    protected List<@FixedValue(classes = Direction.class) String> directions;

    protected boolean disableSort;

    public Sort toSort() {
        return toSort(false, null);
    }

    public Sort toSort(String defaultField, String defaultDirection) {
        if (field == null && fields == null) field = defaultField;
        if (direction == null && directions == null) direction = defaultDirection;
        return toSort();
    }

    public Sort toSort(boolean isNative, @Nullable String prefix) {
        if (field == null) field = "createdAt";
        if (direction == null) direction = "desc";
        Sort sort;
        if (fields != null) {
            if (directions != null) {
                List<Sort.Order> orders = new ArrayList<>();
                for (int i = 0; i < fields.size(); i++) {
                    orders.add(new Sort.Order(Sort.Direction.fromString(directions.get(i)), convertedFieldName(fields.get(i), prefix, isNative)));
                }
                sort = Sort.by(orders);
            } else {
                sort = Sort.by(Sort.Direction.fromString(direction), fields.stream().map(val -> convertedFieldName(val, prefix, isNative))
                        .toList().toArray(new String[0]));
            }
        } else {
            sort = Sort.by(Sort.Direction.fromString(direction), convertedFieldName(field, prefix, isNative));
        }
        return sort;
    }

    protected static String convertedFieldName(String field, String prefix, boolean isNative) {
        field = isNative ? StringUtils.camelCaseToSnakeCase(field) : StringUtils.snakeCaseToCamelCase(field);
        if (prefix != null) {
            return prefix + "." + field;
        } else {
            return field;
        }
    }

    public enum Direction {
        ASC("asc"),
        DESC("desc");
        private final String value;
        Direction(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    @JsonIgnore @Deprecated
    public BaseListParam<T> getBaseListParam() {
        return this.toBuilder().build();
    }

    public BaseListParam<T> toBaseListParam() {
        return this.toBuilder().build();
    }

    @JsonIgnore @Deprecated
    public ListParam getListParam() {
        return ListParam.builder()
                .field(field)
                .fields(fields)
                .direction(direction)
                .directions(directions)
                .disableSort(disableSort)
                .build();
    }

    public ListParam toListParam() {
        return ListParam.builder()
                .field(field)
                .fields(fields)
                .direction(direction)
                .directions(directions)
                .disableSort(disableSort)
                .build();
    }
}

package com.sample.base_project.common.utils.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.base_project.common.base.BaseEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Data
public class BasePageParam<T extends BaseEntity> extends BaseListParam<T> implements Serializable {
    @Min(1)
    protected Integer page = 1;

    @Min(1) @Max(1000)
    protected Integer limit = 10;

    public Pageable toPageable(String defaultField, String defaultDirection) {
        if (field == null && fields == null) field = defaultField;
        if (direction == null && directions == null) direction = defaultDirection;
        return toPageable();
    }

    public Pageable toPageable(List<String> defaultFields, List<String> defaultDirections) {
        if (field == null && fields == null) fields = defaultFields;
        if (direction == null && directions == null) directions = defaultDirections;
        return toPageable();
    }

    public Pageable toPageable() {
        return toPageable(false);
    }

    public Pageable toPageable(boolean isNative) {
        return toPageable(isNative, false);
    }

    public Pageable toPageable(boolean isNative, boolean disableSort) {
        return toPageable(isNative, disableSort, null);
    }

    public Pageable toPageable(@Nullable String prefix) {
        return toPageable(false, disableSort, prefix);
    }

    public Pageable toPageable(boolean isNative, boolean disableSort, @Nullable String prefix) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        if (disableSort) {
            return PageRequest.of(page - 1, limit);
        }
        Sort sort = toSort(isNative, prefix);

        return PageRequest.of(page - 1, limit, sort);
    }

    public BasePageParam<T> nextPage() {
        if (this.page == null) this.page = 1;
        this.page += 1;
        return this;
    }

    public Integer getPage() {
        if (page == null || page <= 0) {
            return 1;
        }
        return page;
    }

    public Integer getLimit() {
        if (limit == null || limit <= 0) {
            return 10;
        }
        return limit;
    }

    @JsonIgnore @Deprecated
    public BasePageParam<T> getBasePageParam() {
        return this.toBuilder().build();
    }

    public BasePageParam<T> toBasePageParam() {
        return this.toBuilder().build();
    }

    @JsonIgnore @Deprecated
    public PageParam getPageParam() {
        return PageParam.builder()
                .page(page)
                .limit(limit)
                .field(field)
                .fields(fields)
                .direction(direction)
                .directions(directions)
                .disableSort(disableSort)
                .build();
    }

    public PageParam toPageParam() {
        return PageParam.builder()
                .page(page)
                .limit(limit)
                .field(field)
                .fields(fields)
                .direction(direction)
                .directions(directions)
                .disableSort(disableSort)
                .build();
    }

    public void setPageParam(BasePageParam<T> pageParam) {
        this.setPage(pageParam.getPage());
        this.setLimit(pageParam.getLimit());
        this.setField(pageParam.getField());
        this.setFields(pageParam.getFields());
        this.setDirection(pageParam.getDirection());
        this.setDirections(pageParam.getDirections());
    }

    public void setPageParam(PageParam pageParam) {
        this.setPage(pageParam.getPage());
        this.setLimit(pageParam.getLimit());
        this.setField(pageParam.getField());
        this.setFields(pageParam.getFields());
        this.setDirection(pageParam.getDirection());
        this.setDirections(pageParam.getDirections());
    }
}

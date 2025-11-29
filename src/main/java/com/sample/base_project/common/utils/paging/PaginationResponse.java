package com.sample.base_project.common.utils.paging;

import com.sample.base_project.common.base.BaseEntity;
import com.sample.base_project.common.base.view.GetBasePageParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PaginationResponse<T> implements Serializable {
    private int page;
    private int pageCount;
    private int limit;
    private long totalItems;
    private List<T> data;

    public PaginationResponse(
            int page,
            int pageCount,
            int limit,
            long totalItems,
            List<T> data) {
        this.page = page;
        this.pageCount = pageCount;
        this.limit = limit;
        this.totalItems = totalItems;
        this.data = data;
    }

    public static <T> PaginationResponse<T> empty(PageParam pageParam) {
        return new PaginationResponse<>(pageParam.getPage(), 0, pageParam.getLimit(), 0, Collections.emptyList());
    }

    public static <T> PaginationResponse<T> emptyDefault() {
        return new PaginationResponse<>(1, 0, 10, 0, Collections.emptyList());
    }

    public static <T> PaginationResponse<T> of(Page<T> page) {
        if (page == null) {
            return PaginationResponse.emptyDefault();
        }

        return new PaginationResponse<>(
                page.getNumber() + 1,
                page.getTotalPages(),
                page.getSize(),
                page.getTotalElements(),
                page.getContent());
    }

    public <U> PaginationResponse<U> map(Function<? super T, ? extends U> converter) {
        Assert.notNull(converter, "Function must not be null");
        List<U> list = this.data.stream().map(converter).collect(Collectors.toList());
        return new PaginationResponse<>(this.page, this.pageCount, this.limit, this.totalItems, list);
    }

    public <U> PaginationResponse<U> mapAll(Function<List<T>, List<U>> converter) {
        Assert.notNull(converter, "Function must not be null");
        List<U> list = converter.apply(this.data);
        return new PaginationResponse<>(this.page, this.pageCount, this.limit, this.totalItems, list);
    }

    public PaginationResponse<T> forEach(Consumer<? super T> action) {
        Assert.notNull(action, "action must not be null");
        this.data.forEach(action);
        return this;
    }

    public Optional<T> findOne() {
        if (this.data == null || this.data.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.data.get(0));
    }

    public boolean hasData() {
        return data != null && !data.isEmpty();
    }


    public boolean hasNext() {
        return page < pageCount;
    }

    public void set(PaginationResponse<T> response) {
        this.page = response.getPage();
        this.pageCount = response.getPageCount();
        this.limit = response.getLimit();
        this.totalItems = response.getTotalItems();
        this.data = response.getData();
    }

    public <B extends BaseEntity> void next(Function<GetBasePageParam<B>, PaginationResponse<T>> function, GetBasePageParam<B> pageParam) {
        if (hasNext()) {
            PaginationResponse<T> response = function.apply(pageParam.nextPage());
            this.set(response);
        } else {
            this.page += 1;
            this.data = new ArrayList<>();
        }
    }

    public static <B extends BaseEntity> void loopExecuteAll(Function<GetBasePageParam<B>, PaginationResponse<B>> function, GetBasePageParam<B> pageParam, Consumer<List<B>> consumer) {
        PaginationResponse<B> response = function.apply(pageParam);
        while (response.hasData()) {
            consumer.accept(response.getData());
            if (response.hasNext()) {
                response.next(function, pageParam);
            } else {
                break;
            }
        }
    }

    public static <B extends BaseEntity> void loopExecuteEach(Function<GetBasePageParam<B>, PaginationResponse<B>> function, GetBasePageParam<B> pageParam, Consumer<B> consumer) {
        PaginationResponse<B> response = function.apply(pageParam);
        while (response.hasData()) {
            response.forEach(consumer);
            if (response.hasNext()) {
                response.next(function, pageParam);
            } else {
                break;
            }
        }
    }
}

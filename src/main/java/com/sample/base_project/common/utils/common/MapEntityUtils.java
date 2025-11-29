package com.sample.base_project.common.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapEntityUtils {

    public static <RID, K, V> Map<K, V> getMapAll(Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                  List<RID> listRef,
                                                  Function<V, K> getRefKeyFunc) {
        if (listRef == null || listRef.isEmpty()) {
            return Map.of();
        }
        return findListValueByKeyRefFunc.apply(listRef).stream().collect(Collectors.toMap(getRefKeyFunc, Function.identity()));
    }

    public static <RID, K, V> Map<K, List<V>> getMapAllList(Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                            List<RID> listRef,
                                                            Function<V, K> getRefKeyFunc) {
        if (listRef == null || listRef.isEmpty()) {
            return Map.of();
        }
        return findListValueByKeyRefFunc.apply(listRef).stream().collect(Collectors.groupingBy(getRefKeyFunc));
    }


    public static <T, RID, K, V> Map<K, V> getMapAll(List<T> entityList,
                                                     Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                     Function<T, RID> getEntityRefFunc,
                                                     Function<V, K> getRefKeyFunc) {
        List<RID> listRef = new ArrayList<>(entityList.stream().map(getEntityRefFunc).distinct().filter(Objects::nonNull).toList());
        return getMapAll(findListValueByKeyRefFunc, listRef, getRefKeyFunc);
    }

    public static <T, RID, K, V> Map<K, List<V>> getMapAllList(List<T> entityList,
                                                               Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                               Function<T, RID> getEntityRefFunc,
                                                               Function<V, K> getRefKeyFunc) {
        List<RID> listRef = new ArrayList<>(entityList.stream().map(getEntityRefFunc).distinct().filter(Objects::nonNull).toList());
        return getMapAllList(findListValueByKeyRefFunc, listRef, getRefKeyFunc);
    }

    public static <T, K1, V1> void setMapAllParam(List<T> entityList,
                                                  MapParam<T, K1, V1> param
    ) {
        Function<T, Boolean> filterFunc = param.filter();
        BiConsumer<T, V1> setFunc = param.setRefFunc();
        Function<Void, Map<K1, V1>> mapAllFunc = param.mapAll();

        if (filterFunc == null || setFunc == null || mapAllFunc == null) {
            return;
        }

        Map<K1, V1> mapAll = param.mapAll().apply(null);

        if (mapAll == null || mapAll.isEmpty()) {
            return;
        }

        entityList.stream()
                .filter(filterFunc::apply)
                .forEach(val -> {
                    K1 key = param.getKeyFunc().apply(val);
                    if (key != null && mapAll.containsKey(key)) {
                        setFunc.accept(val, mapAll.get(key));
                    }
                });
    }

    public static <T, K1, V1> void setMapAllParam(List<T> entityList,
                                                  Set<String> mapKeyInclude,
                                                  Set<String> mapKeyExclude,
                                                  String key,
                                                  Function<List<K1>, List<V1>> findListValueByKeyRefFunc,
                                                  Function<T, K1> getEntityRefKeyFunc,
                                                  BiConsumer<T, V1> setRefFunc,
                                                  Function<V1, K1> getRefKeyFunc
    ) {
        MapParam<T, K1, V1> param = MapParam.of(key, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        if (needProcess(param, mapKeyInclude, mapKeyExclude)) {
            setMapAllParam(entityList, param);
        }
    }

    public static <T, K1, V1> void setMapAllParam(List<T> entityList,
                                                  Function<List<K1>, List<V1>> findListValueByKeyRefFunc,
                                                  Function<T, K1> getEntityRefKeyFunc,
                                                  BiConsumer<T, V1> setRefFunc,
                                                  Function<V1, K1> getRefKeyFunc
    ) {
        MapParam<T, K1, V1> param = MapParam.of(entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        setMapAllParam(entityList, param);
    }

    public static <T, K1, V1> void setMapAllListParam(List<T> entityList,
                                                      Set<String> mapKeyInclude,
                                                      Set<String> mapKeyExclude,
                                                      String key,
                                                      Function<List<K1>, List<V1>> findListValueByKeyRefFunc,
                                                      Function<T, K1> getEntityRefKeyFunc,
                                                      BiConsumer<T, List<V1>> setRefFunc,
                                                      Function<V1, K1> getRefKeyFunc
    ) {
        MapParam<T, K1, List<V1>> param = MapParam.ofList(key, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        if (needProcess(param, mapKeyInclude, mapKeyExclude)) {
            setMapAllParam(entityList, param);
        }
    }

    public static <T, K1, V1> void setMapAllListParam(List<T> entityList,
                                                      Function<List<K1>, List<V1>> findListValueByKeyRefFunc,
                                                      Function<T, K1> getEntityRefKeyFunc,
                                                      BiConsumer<T, List<V1>> setRefFunc,
                                                      Function<V1, K1> getRefKeyFunc
    ) {
        MapParam<T, K1, List<V1>> param = MapParam.ofList(entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        setMapAllParam(entityList, param);
    }

    @SafeVarargs
    public static <T> void setMapAllListParam(List<T> entityList,
                                              MapParam<T, ?, ?>... param1
    ) {
        List<MapParam<T, ?, ?>> list = List.of(param1);
        list.parallelStream().forEach(param -> {
            if (needProcess(param, null, null)) {
                setMapAllListParam(entityList, param);
            }
        });
    }

    @SafeVarargs
    public static <T> void setMapAllParam(List<T> entityList,
                                          MapParam<T, ?, ?>... param1
    ) {
        List<MapParam<T, ?, ?>> list = List.of(param1);
        list.parallelStream().forEach(param -> {
            if (needProcess(param, null, null)) {
                setMapAllParam(entityList, param);
            }
        });
    }

    @SafeVarargs
    public static <T> void setMapAllParam(List<T> entityList,
                                          Set<String> mapKeyInclude,
                                          Set<String> mapKeyExclude,
                                          MapParam<T, ?, ?>... param1
    ) {
        List<MapParam<T, ?, ?>> list = List.of(param1);
        list.parallelStream().forEach(param -> {
            if (needProcess(param, mapKeyInclude, mapKeyExclude)) {
                setMapAllParam(entityList, param);
            }
        });
    }

    private static boolean needProcess(MapParam<?, ?, ?> param, Set<String> mapKeyInclude, Set<String> mapKeyExclude) {
        if (param != null) {
            if (param.key() == null || param.key().equals("-")) {
                return true;
            } else {
                if (mapKeyExclude != null && !mapKeyExclude.isEmpty()) {
                    if (mapKeyExclude.contains(param.key())) {
                        return false;
                    }
                }
                if (mapKeyInclude != null && !mapKeyInclude.isEmpty()) {
                    return mapKeyInclude.contains(param.key());
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public record MapParam<T, K, V>(String key,
                                    Function<T, Boolean> filter,
                                    Function<Void, Map<K, V>> mapAll,
                                    Function<T, K> getKeyFunc,
                                    BiConsumer<T, V> setRefFunc) {

        // -----------------------------
        // BASE
        // -----------------------------
        public static <T, K, V> MapParam<T, K, V> of(String key,
                                                     Function<T, Boolean> filter,
                                                     Function<Void, Map<K, V>> mapAll,
                                                     Function<T, K> getKeyFunc,
                                                     BiConsumer<T, V> setRefFunc) {
            return new MapParam<>(key, filter, mapAll, getKeyFunc, setRefFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(Function<T, Boolean> filter,
                                                     Function<Void, Map<K, V>> mapAll,
                                                     Function<T, K> getKeyFunc,
                                                     BiConsumer<T, V> setRefFunc) {
            return of("-", filter, mapAll, getKeyFunc, setRefFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(String key,
                                                     Function<Void, Map<K, V>> mapAll,
                                                     Function<T, K> getKeyFunc,
                                                     BiConsumer<T, V> setRefFunc) {
            return of(key, val -> true, mapAll, getKeyFunc, setRefFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(Function<Void, Map<K, V>> mapAll,
                                                     Function<T, K> getKeyFunc,
                                                     BiConsumer<T, V> setRefFunc) {
            return of(val -> true, mapAll, getKeyFunc, setRefFunc);
        }

        // -----------------------------
        // SINGLE - NO SUB
        // -----------------------------
        public static <T, RID, K, V> MapParam<T, K, V> of(String key,
                                                          Function<T, Boolean> filter,
                                                          List<T> entityList,
                                                          Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                          Function<T, RID> getEntityRefFunc,
                                                          Function<T, K> getEntityRefKeyFunc,
                                                          BiConsumer<T, V> setRefFunc,
                                                          Function<V, K> getRefKeyFunc) {
            Function<Void, Map<K, V>> map = (val) -> getMapAll(entityList, findListValueByKeyRefFunc, getEntityRefFunc, getRefKeyFunc);
            return of(key, filter, map, getEntityRefKeyFunc, setRefFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, V> of(Function<T, Boolean> filter,
                                                          List<T> entityList,
                                                          Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                          Function<T, RID> getEntityRefFunc,
                                                          Function<T, K> getEntityRefKeyFunc,
                                                          BiConsumer<T, V> setRefFunc,
                                                          Function<V, K> getRefKeyFunc) {
            Function<Void, Map<K, V>> map = (val) -> getMapAll(entityList, findListValueByKeyRefFunc, getEntityRefFunc, getRefKeyFunc);
            return of(filter, map, getEntityRefKeyFunc, setRefFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, V> of(String key,
                                                          List<T> entityList,
                                                          Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                          Function<T, RID> getEntityRefFunc,
                                                          Function<T, K> getEntityRefKeyFunc,
                                                          BiConsumer<T, V> setRefFunc,
                                                          Function<V, K> getRefKeyFunc) {
            return of(key, val -> true, entityList, findListValueByKeyRefFunc, getEntityRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, V> of(List<T> entityList,
                                                          Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                          Function<T, RID> getEntityRefFunc,
                                                          Function<T, K> getEntityRefKeyFunc,
                                                          BiConsumer<T, V> setRefFunc,
                                                          Function<V, K> getRefKeyFunc) {
            return of(val -> true, entityList, findListValueByKeyRefFunc, getEntityRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(String key,
                                                     Function<T, Boolean> filter,
                                                     List<T> entityList,
                                                     Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                     Function<T, K> getEntityRefKeyFunc,
                                                     BiConsumer<T, V> setRefFunc,
                                                     Function<V, K> getRefKeyFunc) {
            return of(key, filter, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(Function<T, Boolean> filter,
                                                     List<T> entityList,
                                                     Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                     Function<T, K> getEntityRefKeyFunc,
                                                     BiConsumer<T, V> setRefFunc,
                                                     Function<V, K> getRefKeyFunc) {
            return of(filter, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(String key,
                                                     List<T> entityList,
                                                     Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                     Function<T, K> getEntityRefKeyFunc,
                                                     BiConsumer<T, V> setRefFunc,
                                                     Function<V, K> getRefKeyFunc) {
            return of(key, val -> true, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, V> of(List<T> entityList,
                                                     Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                     Function<T, K> getEntityRefKeyFunc,
                                                     BiConsumer<T, V> setRefFunc,
                                                     Function<V, K> getRefKeyFunc) {
            return of(val -> true, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        // -----------------------------
        // LIST - NO SUB
        // -----------------------------
        public static <T, RID, K, V> MapParam<T, K, List<V>> ofList(String key,
                                                                    Function<T, Boolean> filter,
                                                                    List<T> entityList,
                                                                    Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                                    Function<T, RID> getEntityRefFunc,
                                                                    Function<T, K> getEntityRefKeyFunc,
                                                                    BiConsumer<T, List<V>> setRefFunc,
                                                                    Function<V, K> getRefKeyFunc) {

            Function<Void, Map<K, List<V>>> mapList = (val) -> getMapAllList(entityList, findListValueByKeyRefFunc, getEntityRefFunc, getRefKeyFunc);
            return of(key, filter, mapList, getEntityRefKeyFunc, setRefFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, List<V>> ofList(Function<T, Boolean> filter,
                                                                    List<T> entityList,
                                                                    Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                                    Function<T, RID> getEntityRefFunc,
                                                                    Function<T, K> getEntityRefKeyFunc,
                                                                    BiConsumer<T, List<V>> setRefFunc,
                                                                    Function<V, K> getRefKeyFunc) {

            Function<Void, Map<K, List<V>>> mapList = (val) -> getMapAllList(entityList, findListValueByKeyRefFunc, getEntityRefFunc, getRefKeyFunc);
            return of(filter, mapList, getEntityRefKeyFunc, setRefFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, List<V>> ofList(String key,
                                                                    List<T> entityList,
                                                                    Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                                    Function<T, RID> getEntityRefFunc,
                                                                    Function<T, K> getEntityRefKeyFunc,
                                                                    BiConsumer<T, List<V>> setRefFunc,
                                                                    Function<V, K> getRefKeyFunc) {
            return ofList(key, val -> true, entityList, findListValueByKeyRefFunc, getEntityRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, RID, K, V> MapParam<T, K, List<V>> ofList(List<T> entityList,
                                                                    Function<List<RID>, List<V>> findListValueByKeyRefFunc,
                                                                    Function<T, RID> getEntityRefFunc,
                                                                    Function<T, K> getEntityRefKeyFunc,
                                                                    BiConsumer<T, List<V>> setRefFunc,
                                                                    Function<V, K> getRefKeyFunc) {
            return ofList(val -> true, entityList, findListValueByKeyRefFunc, getEntityRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, List<V>> ofList(String key,
                                                               Function<T, Boolean> filter,
                                                               List<T> entityList,
                                                               Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                               Function<T, K> getEntityRefKeyFunc,
                                                               BiConsumer<T, List<V>> setRefFunc,
                                                               Function<V, K> getRefKeyFunc) {
            return ofList(key, filter, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, List<V>> ofList(Function<T, Boolean> filter,
                                                               List<T> entityList,
                                                               Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                               Function<T, K> getEntityRefKeyFunc,
                                                               BiConsumer<T, List<V>> setRefFunc,
                                                               Function<V, K> getRefKeyFunc) {
            return ofList(filter, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, List<V>> ofList(String key,
                                                               List<T> entityList,
                                                               Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                               Function<T, K> getEntityRefKeyFunc,
                                                               BiConsumer<T, List<V>> setRefFunc,
                                                               Function<V, K> getRefKeyFunc) {
            return ofList(key, val -> true, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }

        public static <T, K, V> MapParam<T, K, List<V>> ofList(List<T> entityList,
                                                               Function<List<K>, List<V>> findListValueByKeyRefFunc,
                                                               Function<T, K> getEntityRefKeyFunc,
                                                               BiConsumer<T, List<V>> setRefFunc,
                                                               Function<V, K> getRefKeyFunc) {
            return ofList(val -> true, entityList, findListValueByKeyRefFunc, getEntityRefKeyFunc, setRefFunc, getRefKeyFunc);
        }
    }
}

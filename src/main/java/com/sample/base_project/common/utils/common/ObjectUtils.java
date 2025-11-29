package com.sample.base_project.common.utils.common;

import org.hibernate.LazyInitializationException;

import java.util.function.Function;

public class ObjectUtils {

    private static <T, U> U execute(T value, Function<Void, ? extends U> function) {
        try {
            if (value == null) {
                return null;
            } else {
                return function.apply(null);
            }
        } catch (NullPointerException | LazyInitializationException e) {
            return null;
        }
    }

    public static <T> T getNullSafe(T value) {
        return execute(value, unused -> value);
    }

    public static <T, U> U getNullSafe(T value, Function<? super T, ? extends U> converter) {
        return execute(value, unused -> converter.apply(value));
    }

    public static <T, U, V> V getNullSafe(T value, Function<? super T, ? extends U> converter, Function<? super U, ? extends V> converter2) {
        return execute(value, unused -> getNullSafe(converter.apply(value), converter2));
    }

    public static <T, U, V, W> W getNullSafe(T value,
                                              Function<? super T, ? extends U> converter,
                                              Function<? super U, ? extends V> converter2,
                                              Function<? super V, ? extends W> converter3) {
        return execute(value, unused -> getNullSafe(converter.apply(value), converter2, converter3));
    }

    public static <T, U, V, W, X> X getNullSafe(T value,
                                                 Function<? super T, ? extends U> converter,
                                                 Function<? super U, ? extends V> converter2,
                                                 Function<? super V, ? extends W> converter3,
                                                 Function<? super W, ? extends X> converter4) {
        return execute(value, unused -> getNullSafe(converter.apply(value), converter2, converter3, converter4));
    }

    public static <T, U, V, W, X, Y> Y getNullSafe(T value,
                                                   Function<? super T, ? extends U> converter,
                                                   Function<? super U, ? extends V> converter2,
                                                   Function<? super V, ? extends W> converter3,
                                                   Function<? super W, ? extends X> converter4,
                                                   Function<? super X, ? extends Y> converter5) {
        return execute(value, unused -> getNullSafe(converter.apply(value), converter2, converter3, converter4, converter5));
    }

    public static <T, U, V, W, X, Y, Z> Z getNullSafe(T value,
                                                      Function<? super T, ? extends U> converter,
                                                      Function<? super U, ? extends V> converter2,
                                                      Function<? super V, ? extends W> converter3,
                                                      Function<? super W, ? extends X> converter4,
                                                      Function<? super X, ? extends Y> converter5,
                                                      Function<? super Y, ? extends Z> converter6) {
        return execute(value, unused -> getNullSafe(converter.apply(value), converter2, converter3, converter4, converter5, converter6));
    }


    public static String toString(Object object) {
        return toStringNullable(object);
    }

    public static String toStringNullable(Object object) {
        return getNullSafe(object, Object::toString);
    }

    public static boolean isNull(Object... object) {
        for (Object o : object) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotNull(Object... object) {
        return !isNull(object);
    }
}

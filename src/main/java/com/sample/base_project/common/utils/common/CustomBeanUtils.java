package com.sample.base_project.common.utils.common;

import jakarta.annotation.Nullable;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomBeanUtils {

    public static void copyFieldNotNull(Object source, Object target, String... excludeFields) throws InvocationTargetException, IllegalAccessException {
        String[] ignoreProperties = findNullProperties(source, excludeFields);
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static <T> void copyFieldNotNull(T source, T target, @Nullable T targetCopy, String... excludeFields) throws InvocationTargetException, IllegalAccessException {
        copyFieldNotNull(source, target, targetCopy, Set.of(excludeFields));
    }

    public static <T> void copyFieldNotNull(T source, T target, T targetCopy, Set<String> excludeFields) throws InvocationTargetException, IllegalAccessException {
        String[] ignoreProperties = findNullProperties(source, excludeFields);
        if (targetCopy != null) {
            BeanUtils.copyProperties(target, targetCopy, ignoreProperties);
        }
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    private static String[] findNullProperties(Object source, String... excludeFields) throws InvocationTargetException, IllegalAccessException {
        Set<String> excludeFieldsSet = new HashSet<>(Arrays.asList(excludeFields));
        return findNullProperties(source, excludeFieldsSet);
    }

    private static String[] findNullProperties(Object source, Set<String> excludeFields) throws InvocationTargetException, IllegalAccessException {
        List<String> nullProps = new ArrayList<>();
        for (var prop : BeanUtils.getPropertyDescriptors(source.getClass())) {
            Object obj = prop.getReadMethod().invoke(source);
            if (obj == null) {
                if (excludeFields != null && excludeFields.contains(prop.getName())) {
                    continue;
                }
                nullProps.add(prop.getName());
            }
        }
        return nullProps.toArray(String[]::new);
    }
}

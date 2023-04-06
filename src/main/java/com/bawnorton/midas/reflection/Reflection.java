package com.bawnorton.midas.reflection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Reflection {
    public static <T> List<T> getFields(Class<?> clazz, String containsString, Class<T> type) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getName().contains(containsString))
                .peek(f -> f.setAccessible(true))
                .map(f -> {
                    try {
                        return type.cast(f.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }
}

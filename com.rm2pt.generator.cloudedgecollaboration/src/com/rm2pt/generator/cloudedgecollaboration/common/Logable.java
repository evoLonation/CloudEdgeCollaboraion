package com.rm2pt.generator.cloudedgecollaboration.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Logable {
    @Override
    public String toString() {
        Class<?> clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        String fieldString = Arrays.stream(fields)
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(this);
                        return f.getName() + "=" + value;
                    } catch (IllegalAccessException e) {
                        return f.getName() + "=<ERROR>";
                    }
                })
                .collect(Collectors.joining(", \n"));
        return clazz.getSimpleName() + "{" + fieldString + "}\n";
    }
}

package com.rm2pt.generator.cloudedgecollaboration.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Logable {
    static private int tab = 0;
    @Override
    public String toString() {
        Class<?> clazz = getClass();
        Field[] fields = clazz.getDeclaredFields();
        String tabStr = "\t".repeat(tab);
        String fieldString = Arrays.stream(fields)
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(this);
                        if(! (value instanceof Logable)){
                            return tabStr + f.getName() + "=" + value;
                        }else {
                            tab ++;
                            String ret = tabStr + f.getName() + ":\n" + value;
                            tab --;
                            return ret;
                        }
                    } catch (IllegalAccessException e) {
                        return f.getName() + "=<ERROR>";
                    }
                })
                .collect(Collectors.joining("," + tabStr +"\n"));
        return tabStr + clazz.getSimpleName() + "\n" +  fieldString + "\n";
    }
}

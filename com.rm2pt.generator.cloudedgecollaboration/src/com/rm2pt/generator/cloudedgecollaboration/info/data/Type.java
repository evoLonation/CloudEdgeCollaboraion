package com.rm2pt.generator.cloudedgecollaboration.info.data;

//通常用于表示entity或者基本类型，及其数组
public class Type {
    private String name;
    private boolean isEntity;
    private boolean isMulti;

    public String getName() {
        return name;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public boolean isMulti() {
        return isMulti;
    }
}

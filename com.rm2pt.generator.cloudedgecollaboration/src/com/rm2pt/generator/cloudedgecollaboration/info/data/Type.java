package com.rm2pt.generator.cloudedgecollaboration.info.data;

//通常用于表示entity或者基本类型，及其数组
public abstract class Type {
    private String name;
    private boolean isMulti;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public boolean isMulti() {
        return isMulti;
    }
}

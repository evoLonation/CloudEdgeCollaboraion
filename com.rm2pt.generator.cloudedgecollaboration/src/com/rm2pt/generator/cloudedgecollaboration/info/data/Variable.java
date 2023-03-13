package com.rm2pt.generator.cloudedgecollaboration.info.data;



public class Variable {
    private String name;
    private Type type;
    public void setName(String name){
        this.name = name;
    }
    public void setType(Type type){
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}

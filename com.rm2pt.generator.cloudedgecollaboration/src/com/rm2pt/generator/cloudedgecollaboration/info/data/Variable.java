package com.rm2pt.generator.cloudedgecollaboration.info.data;
// todo change data package


/**
 * 如果存在对一个对象的关联的引用
 * 如 doctor.Patients
 * 则创建一个variable叫
 * doctor_Patients
 */
public class Variable {
    private String name;
    private Type type;
    public enum ScopeType {
        PARAM,
        GLOBAL,
        DEFINITION,
        LET,
        GENERATE,
        RETURN,
        INTERNAL,
    }
    private ScopeType scopeType;

    public Variable(String name, Type type, ScopeType scopeType) {
        this.name = name;
        this.type = type;
        this.scopeType = scopeType;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}

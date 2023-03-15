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

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }
}

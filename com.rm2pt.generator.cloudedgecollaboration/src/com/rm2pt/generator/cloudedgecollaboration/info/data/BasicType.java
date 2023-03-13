package com.rm2pt.generator.cloudedgecollaboration.info.data;

public abstract class BasicType extends Type{
    private TypeEnum typeEnum;
    public enum TypeEnum {
        STRING,
        REAL,
        INTEGER,
        TIME;
    }
}

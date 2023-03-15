package com.rm2pt.generator.cloudedgecollaboration.info.data;

public class BasicType extends Type{
    private TypeEnum typeEnum;
    public enum TypeEnum {
        STRING,
        REAL,
        INTEGER,
        TIME,
        BOOLEAN,
    }

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
}

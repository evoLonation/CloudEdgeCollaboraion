package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;

public class LiteralValue extends UnaryValue {
    private String value;
    private BasicType type;
    public LiteralValue(String value, BasicType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public BasicType getType() {
        return type;
    }
}

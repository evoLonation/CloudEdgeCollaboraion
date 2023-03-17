package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;

public class AtomicCondition extends Condition {
    private String attributeName;
    private Value value;
    private OP op;

    public enum OP {
        EQUALS,
        BT,
        LT,
        BQ,
        LQ,
    }

    public AtomicCondition(String attributeName, Value value, OP op) {
        this.attributeName = attributeName;
        this.value = value;
        this.op = op;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Value getValue() {
        return value;
    }

    public OP getOp() {
        return op;
    }
}

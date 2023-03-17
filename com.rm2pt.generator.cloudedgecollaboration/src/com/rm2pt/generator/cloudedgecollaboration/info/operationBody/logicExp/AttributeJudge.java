package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;

public class AttributeJudge extends AtomicExp {
    private Value left;
    private Value right;
    private OP op;
    public enum OP{
        EQ,
        LT,
        GT,
        LE,
        GE,
    }

}

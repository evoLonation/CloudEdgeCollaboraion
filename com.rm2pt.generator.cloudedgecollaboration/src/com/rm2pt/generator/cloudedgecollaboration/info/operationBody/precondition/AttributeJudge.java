package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;

public class AttributeJudge extends BoolExp{
    private Value left;
    private Value right;
    public enum OP{
        AND,
        OR,
    }
    private OP op;

}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeRef;

public class AtomicExp extends LogicExp {
    private AttributeRef left;
    private AttributeRef right;
    private OP op;

    @Override
    public boolean mustPrecondition() {
        return false;
    }
    public enum OP{
        EQ,
        LT,
        GT,
        LE,
        GE,
    }

    public AtomicExp(AttributeRef left, AttributeRef right, OP op) {
        this.left = left;
        this.right = right;
        this.op = op;
        check();
    }

    private void check(){
        if(left.getAttribute().getType().getTypeEnum().equals(right.getAttribute().getType().getTypeEnum())){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public AttributeRef getLeft() {
        return left;
    }

    public AttributeRef getRight() {
        return right;
    }

    public OP getOp() {
        return op;
    }
}

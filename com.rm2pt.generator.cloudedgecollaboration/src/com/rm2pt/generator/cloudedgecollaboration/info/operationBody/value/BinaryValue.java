package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;

public class BinaryValue extends RValue{
    private UnaryValue left;
    private UnaryValue right;
    private OP op;

    @Override
    public BasicType getType() {
        return left.getType();
    }

    public enum OP{
        ADD, SUB, MULT, DIV,
    }

    public BinaryValue(UnaryValue left, UnaryValue right, OP op) {
        this.left = left;
        this.right = right;
        this.op = op;
        check();
    }

    private void check(){
        if(left.getType().equals(left.getType())){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public UnaryValue getLeft() {
        return left;
    }

    public UnaryValue getRight() {
        return right;
    }

    public OP getOp() {
        return op;
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;

public class BinaryValue extends RValue{
    private RValue left;
    private RValue right;
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
        if(left.getType().getTypeEnum() == BasicType.TypeEnum.INTEGER && right.getType().getTypeEnum() == BasicType.TypeEnum.REAL){
            this.left = new CastFloat(left);
        }
        if(left.getType().getTypeEnum() == BasicType.TypeEnum.REAL && right.getType().getTypeEnum() == BasicType.TypeEnum.INTEGER){
            this.right = new CastFloat(right);
        }
        check();
    }

    private void check(){
        if(left.getType().equals(right.getType())){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public RValue getLeft() {
        return left;
    }

    public RValue getRight() {
        return right;
    }

    public OP getOp() {
        return op;
    }
}

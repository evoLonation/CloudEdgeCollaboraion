package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.CastFloat;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.RValue;

public class AtomicExp extends LogicExp {
    private RValue left;
    private RValue right;
    private OP op;

    public enum OP{
        EQ,
        LT,
        GT,
        LE,
        GE,
    }

    public AtomicExp(RValue left, RValue right, OP op, ExpType expType) {
        super(expType);
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
        if(left.getType().getTypeEnum().equals(right.getType().getTypeEnum())){
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

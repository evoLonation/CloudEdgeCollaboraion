package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeRef;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.CastFloat;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.RValue;

public class Assign extends Statement {
    private AttributeRef left;
    private RValue right;

    public Assign(AttributeRef left, RValue right) {
        this.left = left;
        this.right = right;
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

    public AttributeRef getLeft() {
        return left;
    }

    public RValue getRight() {
        return right;
    }
}

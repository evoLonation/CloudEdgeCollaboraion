package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;

public class CastFloat extends RValue{
    private RValue rValue;

    public CastFloat(RValue rValue) {
        this.rValue = rValue;
        if(rValue.getType().getTypeEnum() != BasicType.TypeEnum.INTEGER){
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public BasicType getType() {
        return new BasicType(BasicType.TypeEnum.REAL);
    }

    public RValue getrValue() {
        return rValue;
    }
}

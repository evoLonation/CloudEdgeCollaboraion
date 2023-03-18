package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class Limit extends CollectionOp{
    int number;

    public Limit(Variable targetVar, Source source, int number) {
        super(targetVar, source);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

public class Limit extends CollectionOp{
    int number;

    public Limit(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}

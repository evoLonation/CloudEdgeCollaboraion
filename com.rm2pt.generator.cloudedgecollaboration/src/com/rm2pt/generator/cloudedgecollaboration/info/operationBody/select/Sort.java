package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

public class Sort extends CollectionOp{
    private String attributeName;
    private Rule rule;

    public enum Rule {
        ASCENDING,
        DESCENDING,
    }

    public Sort(String attributeName, Rule rule) {
        this.attributeName = attributeName;
        this.rule = rule;
    }
}

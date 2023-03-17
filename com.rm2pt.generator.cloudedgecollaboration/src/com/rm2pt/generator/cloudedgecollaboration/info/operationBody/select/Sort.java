package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;

public class Sort extends CollectionOp{
    private EntityInfo.Attribute attribute;
    private Rule rule;

    public enum Rule {
        ASCENDING,
        DESCENDING,
    }

    public Sort(EntityInfo.Attribute attribute, Rule rule) {
        this.attribute = attribute;
        this.rule = rule;
    }

    public EntityInfo.Attribute getAttribute() {
        return attribute;
    }

    public Rule getRule() {
        return rule;
    }
}

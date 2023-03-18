package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class Sort extends CollectionOp{
    private EntityInfo.Attribute attribute;
    private Rule rule;

    public enum Rule {
        ASCENDING,
        DESCENDING,
    }

    public Sort(Variable targetVar, Source source, EntityInfo.Attribute attribute, Rule rule) {
        super(targetVar, source);
        this.attribute = attribute;
        this.rule = rule;
        check();
    }

    private void check(){
        if(source.getEntityInfo().getKeyType(attribute.getName()) == EntityInfo.KeyType.ATTRIBUTE){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public EntityInfo.Attribute getAttribute() {
        return attribute;
    }

    public Rule getRule() {
        return rule;
    }
}

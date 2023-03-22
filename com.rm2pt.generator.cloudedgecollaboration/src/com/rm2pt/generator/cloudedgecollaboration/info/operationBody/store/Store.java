package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.Set;

public abstract class Store {
    private Variable variable;
    private Set<EntityInfo.Attribute> attributeSet;

    public Store(Variable variable, Set<EntityInfo.Attribute> attributeSet) {
        this.variable = variable;
        this.attributeSet = attributeSet;
    }

    public Variable getVariable() {
        return variable;
    }

    public Set<EntityInfo.Attribute> getAttributeSet() {
        return attributeSet;
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.Set;

public abstract class Query {
    private Variable internalVar;
    protected Variable targetVar;
    protected EntityInfo entityInfo;
    protected Set<EntityInfo.Attribute> attributeSet;

    public Query(Variable targetVar, Set<EntityInfo.Attribute> attributeSet, Variable internalVar) {
        this.targetVar = targetVar;
        this.entityInfo = targetVar.mustGetEntity();
        this.attributeSet = attributeSet;
        this.internalVar = internalVar;
        check();
    }
    private void check(){
        attributeSet.forEach(attribute -> {
            if(entityInfo.getKeyType(attribute.getName()) == null){
                throw new UnsupportedOperationException();
            }
        });
    }

    public Variable getTargetVar() {
        return targetVar;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }
}

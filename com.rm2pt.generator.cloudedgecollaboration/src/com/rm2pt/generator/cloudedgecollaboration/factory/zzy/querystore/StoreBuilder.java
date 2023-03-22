package com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Insert;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Store;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Update;

import java.util.Set;

public class StoreBuilder {
    private Variable variable;
    private Set<EntityInfo.Attribute> attributeSet;

    public StoreBuilder(Variable variable) {
        this.variable = variable;
        check(variable.mustGetEntity() != null);
    }
    public StoreBuilder addChangedAttribute(EntityInfo.Attribute attribute) {
        check(variable.mustGetEntity().isOwner(attribute));
        attributeSet.add(attribute);
        return this;
    }
    public Store build(){
        if(variable.getScopeType() == Variable.ScopeType.LET){
            return new Insert(variable, attributeSet);
        }else{
            return new Update(variable, attributeSet);
        }
    }

    public void check(boolean condition){
        if(!condition){
            throw new UnsupportedOperationException();
        }
    }
}

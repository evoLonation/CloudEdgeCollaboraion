package com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Get;

import java.util.HashSet;
import java.util.Set;

public class GetBuilder {
    private LogicExp logicExp;
    private Variable internalVar;
    private Variable targetVar;
    private Set<EntityInfo.Attribute> attributeSet = new HashSet<>();

    public GetBuilder(LogicExp logicExp, Variable internalVar, Variable targetVar) {
        this.logicExp = logicExp;
        this.internalVar = internalVar;
        this.targetVar = targetVar;
    }
    public GetBuilder addUsedAttribute(EntityInfo.Attribute attribute){
        attributeSet.add(attribute);
        return this;
    }

    public Get build(){
        return new Get(targetVar, logicExp, attributeSet, internalVar);
    }
}

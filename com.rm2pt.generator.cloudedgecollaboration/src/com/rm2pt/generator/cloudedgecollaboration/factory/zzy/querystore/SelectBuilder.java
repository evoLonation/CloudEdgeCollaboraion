package com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Select;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectBuilder {
    private LogicExp logicExp;
    private Integer limit;
    private List<EntityInfo.Attribute> sortList = new ArrayList<>();
    private Variable internalVar;
    private Variable targetVar;
    private Set<EntityInfo.Attribute> attributeSet = new HashSet<>();

    public SelectBuilder(LogicExp logicExp, Variable internalVar, Variable targetVar) {
        this.logicExp = logicExp;
        this.internalVar = internalVar;
        this.targetVar = targetVar;
    }
    public SelectBuilder setLimit(int limit){
        this.limit = limit;
        return this;
    }
    public SelectBuilder addSort(EntityInfo.Attribute attribute){
        sortList.add(attribute);
        return this;
    }
    public SelectBuilder addUsedAttribute(EntityInfo.Attribute attribute){
        attributeSet.add(attribute);
        return this;
    }


    public Select build(){
        return new Select(targetVar, logicExp, limit, sortList, attributeSet, internalVar);
    }
}

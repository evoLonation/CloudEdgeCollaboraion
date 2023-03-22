package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

import java.util.List;
import java.util.Set;

public class Select extends Query {
    private LogicExp logicExp;
    private Integer limit;
    private List<EntityInfo.Attribute> sortList;

    public Select(Variable targetVar, LogicExp logicExp, Integer limit, List<EntityInfo.Attribute> sort, Set<EntityInfo.Attribute> attributeSet, Variable internalVar) {
        super(targetVar, attributeSet, internalVar);
        this.logicExp = logicExp;
        this.limit = limit;
        this.sortList = sort;
    }

    public LogicExp getLogicExp() {
        return logicExp;
    }

    public Integer getLimit() {
        return limit;
    }

    public List<EntityInfo.Attribute> getSort() {
        return sortList;
    }

}

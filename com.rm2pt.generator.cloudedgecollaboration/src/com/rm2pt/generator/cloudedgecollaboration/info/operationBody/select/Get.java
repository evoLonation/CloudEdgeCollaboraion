package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

import java.util.Set;

public class Get extends Query{
    private LogicExp logicExp;

    public Get(Variable targetVar, LogicExp logicExp, Set<EntityInfo.Attribute> attributeSet, Variable internalVar) {
        super(targetVar, attributeSet, internalVar);
        this.logicExp = logicExp;
    }
}

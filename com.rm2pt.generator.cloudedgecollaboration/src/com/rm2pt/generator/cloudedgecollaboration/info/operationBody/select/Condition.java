package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

public class Condition extends CollectionOp{
    private Variable internalVar;
    private LogicExp logicExp;

    public Condition(Variable internalVar, LogicExp logicExp) {
        this.internalVar = internalVar;
        this.logicExp = logicExp;
    }

    public Variable getInternalVar() {
        return internalVar;
    }

    public LogicExp getLogicExp() {
        return logicExp;
    }
}

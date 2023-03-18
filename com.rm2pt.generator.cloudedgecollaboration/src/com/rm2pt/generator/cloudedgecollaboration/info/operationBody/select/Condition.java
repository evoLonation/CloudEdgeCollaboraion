package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

public class Condition extends CollectionOp{
    private Variable internalVar;
    private LogicExp logicExp;
    private boolean isReturnMulti;

    public Condition(Variable targetVar, Source source, Variable internalVar, LogicExp logicExp, boolean isReturnMulti) {
        super(targetVar, source);
        this.internalVar = internalVar;
        this.logicExp = logicExp;
        this.isReturnMulti = isReturnMulti;
    }

    public Variable getInternalVar() {
        return internalVar;
    }

    public LogicExp getLogicExp() {
        return logicExp;
    }

    @Override
    public boolean isReturnMulti() {
        return isReturnMulti;
    }
}

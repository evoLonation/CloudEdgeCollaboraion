package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class GlobalAssign extends Statement{
    private Variable targetVar;
    private Variable sourceVar;

    public GlobalAssign(Variable targetVar, Variable sourceVar) {
        this.targetVar = targetVar;
        this.sourceVar = sourceVar;
        check();
    }
    public void check(){
        if(targetVar.getScopeType() == Variable.ScopeType.GLOBAL){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public Variable getTargetVar() {
        return targetVar;
    }

    public Variable getSourceVar() {
        return sourceVar;
    }
}

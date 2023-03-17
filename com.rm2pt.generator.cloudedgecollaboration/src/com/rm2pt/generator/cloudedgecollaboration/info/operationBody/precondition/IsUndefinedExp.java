package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

// 只有precondition用到
public class IsUndefinedExp extends LogicExp {
    private Variable variable;
    private boolean isUndefined;

    public IsUndefinedExp(Variable variable, boolean isUndefined) {
        this.variable = variable;
        this.isUndefined = isUndefined;
        check();
    }

    private void check(){
        switch (variable.getScopeType()){
            case GLOBAL:
            case DEFINITION:
                return;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public Variable getVariable() {
        return variable;
    }

    public boolean isUndefined() {
        return isUndefined;
    }

    @Override
    public boolean mustPrecondition() {
        return true;
    }
}

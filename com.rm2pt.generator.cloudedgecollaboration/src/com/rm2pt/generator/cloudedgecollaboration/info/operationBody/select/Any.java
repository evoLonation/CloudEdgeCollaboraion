package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class Any {
    private Variable targetVar;
    private Condition condition;
    private Source source;

    public Any(Variable targetVar, Condition condition, Source source) {
        this.targetVar = targetVar;
        this.condition = condition;
        this.source = source;
        check();
    }
    public void check() {
        if(!targetVar.getType().isMulti() && targetVar.mustGetEntity().equals(source.getEntityInfo())){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public Variable getTargetVar() {
        return targetVar;
    }

    public Condition getCondition() {
        return condition;
    }

    public Source getSource() {
        return source;
    }
}

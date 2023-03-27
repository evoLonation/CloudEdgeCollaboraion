package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

import java.util.List;

public class IfBlock extends Statement {
    private LogicExp condition;
    private List<Statement> ifTrue;

    public IfBlock(LogicExp condition, List<Statement> ifTrue) {
        this.condition = condition;
        this.ifTrue = ifTrue;
    }

    public LogicExp getCondition() {
        return condition;
    }

    public List<Statement> getIfTrue() {
        return ifTrue;
    }
}

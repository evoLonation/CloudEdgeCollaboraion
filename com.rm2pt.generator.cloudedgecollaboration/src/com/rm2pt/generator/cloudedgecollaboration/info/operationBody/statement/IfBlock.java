package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

import java.util.List;

public class IfBlock extends Statement {
    private LogicExp condition;
    private List<Statement> ifTrue;
    private List<Statement> elseTrue;
}

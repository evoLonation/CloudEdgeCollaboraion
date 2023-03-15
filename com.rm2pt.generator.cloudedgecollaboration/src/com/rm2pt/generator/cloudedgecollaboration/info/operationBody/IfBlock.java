package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition.Exp;

import java.util.List;

public class IfBlock extends Statement {
    private Exp condition;
    private List<Statement> ifTrue;
    private List<Statement> elseTrue;
}

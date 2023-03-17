package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;

import java.util.List;

public class ForAllOperation extends Statement {
    private Value value;
    private Variable internalVariable;
    private List<Statement> statementList;
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ExpType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;

public class PreCondition extends Statement {
    private LogicExp logicExp;

    public PreCondition(LogicExp logicExp) {
        this.logicExp = logicExp;
        check();
    }
    private void check() {
        if(logicExp.getExpType() == ExpType.PRECONDITION){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public LogicExp getLogicExp() {
        return logicExp;
    }
}

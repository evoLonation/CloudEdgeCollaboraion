package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

import java.util.List;

public class ConnectExp extends LogicExp {
    private List<LogicExp> logicExpList;
    private List<OP> opList;
    private boolean mustPrecondition;

    public enum OP {
        AND,
        OR;
    }

    private void check(){
        if(logicExpList.stream().anyMatch(e -> e.getExpType() != getExpType())){
            throw new UnsupportedOperationException();
        }
        if(logicExpList.size() != opList.size() + 1){
            throw new UnsupportedOperationException();
        }
    }

    public ConnectExp(List<LogicExp> logicExpList, List<OP> opList, ExpType expType) {
        super (expType);
        this.logicExpList = logicExpList;
        this.opList = opList;
        check();
    }

    public List<LogicExp> getLogicExpList() {
        return logicExpList;
    }

    public List<OP> getOpList() {
        return opList;
    }
}

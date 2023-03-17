package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

public class BinaryExp extends LogicExp {
    private LogicExp left;
    private LogicExp right;
    private OP op;

    public enum OP {
        AND,
        OR;
    }

}

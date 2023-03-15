package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition;

public class BinaryExp extends Exp {
    Exp left;

    public enum OP {
        AND,
        OR;
    }

    OP op;
    Exp right;
}

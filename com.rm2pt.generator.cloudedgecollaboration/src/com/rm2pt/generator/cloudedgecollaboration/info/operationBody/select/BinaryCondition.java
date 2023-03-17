package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

public class BinaryCondition extends Condition {
    public enum OP {
        AND,
        OR,
    }

    private Condition left;
    private Condition right;
    private OP op;

    public BinaryCondition(Condition left, Condition right, OP op) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Condition getLeft() {
        return left;
    }

    public Condition getRight() {
        return right;
    }

    public OP getOp() {
        return op;
    }
}

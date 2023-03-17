package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;

public class BinaryExp extends LogicExp {
    private LogicExp left;
    private LogicExp right;
    private OP op;
    private boolean mustPrecondition;

    @Override
    public boolean mustPrecondition() {
        return mustPrecondition;
    }

    public enum OP {
        AND,
        OR;
    }

    public BinaryExp(LogicExp left, LogicExp right, OP op, boolean mustPrecondition) {
        this.left = left;
        this.right = right;
        this.op = op;
        this.mustPrecondition = mustPrecondition;
        check();
    }

    private void check(){
        if(!mustPrecondition){
            if(left.mustPrecondition() || right.mustPrecondition()){
                throw new UnsupportedOperationException();
            }
        }
    }


    public LogicExp getLeft() {
        return left;
    }

    public LogicExp getRight() {
        return right;
    }

    public OP getOp() {
        return op;
    }
}

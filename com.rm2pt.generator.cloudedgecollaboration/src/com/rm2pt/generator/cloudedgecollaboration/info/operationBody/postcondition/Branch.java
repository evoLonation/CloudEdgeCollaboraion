package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.postcondition;

public class Branch extends PostconditionCode {
    private Sequence ifSequence;
    private Sequence elseSequence;

    public Branch(Sequence ifSequence, Sequence elseSequence) {
        this.ifSequence = ifSequence;
        this.elseSequence = elseSequence;
    }

    public Sequence getIfSequence() {
        return ifSequence;
    }

    public Sequence getElseSequence() {
        return elseSequence;
    }
}

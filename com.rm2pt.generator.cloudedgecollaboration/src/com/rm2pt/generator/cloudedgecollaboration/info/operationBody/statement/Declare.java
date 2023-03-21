package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class Declare extends Statement{
    private Variable variable;

    public Declare(Variable variable) {
        this.variable = variable;
        check();
    }
    private void check(){
        variable.mustGetEntity();
        if(!variable.getType().isMulti()){
            return;
        }
        throw new UnsupportedOperationException();
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class BasicVariable extends UnaryValue{
    private Variable variable;

    public BasicVariable(Variable variable) {
        this.variable = variable;
        check();
    }
    private void check(){
        variable.mustGetBasicType();
    }

    @Override
    public BasicType getType() {
        return variable.mustGetBasicType();
    }
}

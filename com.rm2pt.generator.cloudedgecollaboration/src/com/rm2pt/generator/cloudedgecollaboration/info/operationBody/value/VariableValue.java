package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class VariableValue extends LValue {
    private Variable variable;

    public VariableValue(Variable variable) {
        this.variable = variable;
    }
}

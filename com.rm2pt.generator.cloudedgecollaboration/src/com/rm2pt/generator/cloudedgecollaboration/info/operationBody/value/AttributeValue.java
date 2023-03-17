package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class AttributeValue extends LValue{
    private Variable variable;
    private String attribute;

    public AttributeValue(Variable variable, String attribute) {
        this.variable = variable;
        this.attribute = attribute;
    }
}

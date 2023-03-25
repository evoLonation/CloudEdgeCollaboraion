package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.UnaryValue;

public class ResultAssign extends Statement{
    private String value;

    public ResultAssign(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

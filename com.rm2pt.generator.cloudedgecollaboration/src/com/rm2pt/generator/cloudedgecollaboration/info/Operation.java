package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class Operation {
    private String name;
    private List<Variable> inputParamList;
    private Type returnType;
    private OperationBody operationBody;

    public String getName() {
        return name;
    }

    public List<Variable> getInputParamList() {
        return inputParamList;
    }

    public Type getReturnType() {
        return returnType;
    }

    public OperationBody getOperationBody() {
        return operationBody;
    }

    public void setOperationBody(OperationBody operationBody) {
        this.operationBody = operationBody;
    }
}


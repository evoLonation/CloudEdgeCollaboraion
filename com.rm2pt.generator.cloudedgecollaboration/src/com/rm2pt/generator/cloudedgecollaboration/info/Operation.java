package com.rm2pt.generator.cloudedgecollaboration.info;


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

    public void setName(String name) {
        this.name = name;
    }

    public void setInputParamList(List<Variable> inputParamList) {
        this.inputParamList = inputParamList;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public void setOperationBody(OperationBody operationBody) {
        this.operationBody = operationBody;
    }

    public void setOperationBody(OperationBody operationBody) {
        this.operationBody = operationBody;
    }
}


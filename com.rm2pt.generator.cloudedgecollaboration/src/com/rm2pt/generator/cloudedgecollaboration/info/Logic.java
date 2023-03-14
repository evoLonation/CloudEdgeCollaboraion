package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class Logic {
    private Caller caller;
    public enum Caller{
        USER,
        NODE;
    }
    private String name;
    private List<Variable> inputParamList;
    // Nullable
    private Type returnType;
    private LogicBody logicBody;
    private ServiceInfo serviceToCall;

    public Caller getCaller() {
        return caller;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getInputParamList() {
        return inputParamList;
    }

    public Type getReturnType() {
        return returnType;
    }

    public LogicBody getLogicBody() {
        return logicBody;
    }

    public ServiceInfo getServiceToCall() {
        return serviceToCall;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
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

    public void setLogicBody(LogicBody logicBody) {
        this.logicBody = logicBody;
    }

    public void setServiceToCall(ServiceInfo serviceToCall) {
        this.serviceToCall = serviceToCall;
    }
}

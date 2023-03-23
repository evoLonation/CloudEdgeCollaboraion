package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;
import java.util.Objects;

public class ServiceInfo {
    private String name;
    private List<OperationInfo> operationInfoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variable> getGlobalVariableList() {
        return null;
    }

    public List<OperationInfo> getOperationList() {
        return operationInfoList;
    }

    public void setOperationList(List<OperationInfo> operationInfoList) {
        this.operationInfoList = operationInfoList;
    }

    public Location getLocation() {
        return null;
    }

}

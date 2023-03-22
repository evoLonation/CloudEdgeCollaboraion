package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class ServiceInfo {
    private String name;
    // todo delete
    private List<Variable> globalVariableList;
    private List<OperationInfo> operationInfoList;
    // todo delete
    // Nullable
    private Logic logic;
    // todo delete
    private Location location;
    // todo delete
    public enum InteractiveType{
        NORMAL,  // 没有Logic
        LOGICNORETURN,  //其Logic没有返回值
        LOGICTOUSER, //其Logic是有返回值且是由用户发起的消息
        LOGICTONODE;//其Logic是有返回值且是由另一个服务发起的消息
    }
    private InteractiveType interactiveType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Variable> getGlobalVariableList() {
        return globalVariableList;
    }

    public void setGlobalVariableList(List<Variable> globalVariableList) {
        this.globalVariableList = globalVariableList;
    }

    public List<OperationInfo> getOperationList() {
        return operationInfoList;
    }

    public void setOperationList(List<OperationInfo> operationInfoList) {
        this.operationInfoList = operationInfoList;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public InteractiveType getInteractiveType() {
        return interactiveType;
    }

    public void setInteractiveType(InteractiveType interactiveType) {
        this.interactiveType = interactiveType;
    }
}

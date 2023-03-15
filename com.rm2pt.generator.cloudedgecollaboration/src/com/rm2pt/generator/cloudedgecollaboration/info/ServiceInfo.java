package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class ServiceInfo {
    private String name;
    private List<Variable> globalVariableList;
    private List<Operation> operationList;
    // Nullable
    private Logic logic;
    public enum LocationType {
        CLOUD,
        EDGE;
    }
    public enum InteractiveType{
        NORMAL,  // 没有Logic
        LOGICNORETURN,  //其Logic没有返回值
        LOGICTOUSER, //其Logic是有返回值且是由用户发起的消息
        LOGICTONODE;//其Logic是有返回值且是由另一个服务发起的消息
    }
    private LocationType locationType;
    private InteractiveType interactiveType;

    public LocationType getLocationType() {
        return locationType;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getGlobalVariableList() {
        return globalVariableList;
    }

    public List<Operation> getOperationList() {
        return operationList;
    }

    public Logic getLogic() {
        return logic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGlobalVariableList(List<Variable> globalVariableList) {
        this.globalVariableList = globalVariableList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public InteractiveType getInteractiveType() {
        return interactiveType;
    }

    public void setInteractiveType(InteractiveType interactiveType) {
        this.interactiveType = interactiveType;
    }

    // todo
    private Location location;

    public Location getLocation() {
        return location;
    }
}

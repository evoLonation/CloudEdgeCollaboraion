package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class ServiceInfo {
    private String name;
    private List<Variable> globalVariableList;
    private List<Operation> operationList;
    // Nullable
    private Logic logic;

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
}

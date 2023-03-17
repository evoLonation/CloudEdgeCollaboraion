package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableTable {
    public enum VariableType {
        PARAM,
        GLOBAL,
        DEFINITION,
        LET,
        GENERATE,
    }

    private Map<String, Variable> localVariableMap;
    private Map<String, Variable> globalVariableMap;
    private Map<Variable, VariableType> variableTypeMap;
    private Map<Variable, List<String>> variableChangedMap;
    private Map<Variable, List<String>> variableUsedMap;

    public VariableTable(ServiceInfo serviceInfo, Operation operation) {
        localVariableMap = new HashMap<>();
        globalVariableMap = new HashMap<>();
        variableChangedMap = new HashMap<>();
        variableTypeMap = new HashMap<>();
        variableUsedMap = new HashMap<>();

        addGlobalVariable(serviceInfo);
        addParamVariable(operation);
    }
    private void addGlobalVariable(ServiceInfo serviceInfo){
        serviceInfo.getGlobalVariableList()
                .forEach(variable -> {
                    globalVariableMap.put(variable.getName(), variable);
                    variableTypeMap.put(variable, VariableType.GLOBAL);
                });
    }
    private void addParamVariable(Operation operation){
        operation.getInputParamList()
                .forEach(variable -> {
                    localVariableMap.put(variable.getName(), variable);
                    variableTypeMap.put(variable, VariableType.PARAM);
                });
        Variable returnVar = new Variable("result", operation.getReturnType());
        localVariableMap.put(returnVar.getName(), returnVar);
        variableTypeMap.put(returnVar, VariableType.PARAM);
    }

    public void addDefinitionVariable(Variable variable){
        localVariableMap.put(variable.getName(), variable);
        variableTypeMap.put(variable, VariableType.DEFINITION);
    }
    public void addLetVariable(Variable variable){
        localVariableMap.put(variable.getName(), variable);
        variableTypeMap.put(variable, VariableType.LET);
    }
    public void addGenerateVariable(Variable variable){
        localVariableMap.put(variable.getName(), variable);
        variableTypeMap.put(variable, VariableType.GENERATE);
    }

    public void attributeChanged(Variable variable, String attribute){
        variableChangedMap.putIfAbsent(variable, new ArrayList<>());
        variableChangedMap.get(variable).add(attribute);
    }

    public void attributeUsed(Variable variable, String attribute){
        variableUsedMap.putIfAbsent(variable, new ArrayList<>());
        variableUsedMap.get(variable).add(attribute);
    }

    public Variable getGlobalVariable(String name){
        return globalVariableMap.get(name);
    }
    public Variable getLocalVariable(String name){
        return localVariableMap.get(name);
    }
    public VariableType getVariableType(Variable variable){
        return variableTypeMap.get(variable);
    }
    public List<String> getVariableChanged(Variable variable){
        return variableChangedMap.get(variable);
    }
    public List<String> getVariableUsed(Variable variable){
        return variableUsedMap.get(variable);
    }



}

package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableTable {

    private Map<String, Variable> variableMap;
    private Variable internalVar;
    private Map<Variable, List<String>> variableChangedMap;
    private Map<Variable, List<String>> variableUsedMap;

    public VariableTable(ServiceInfo serviceInfo, Operation operation) {
        variableMap = new HashMap<>();
        variableChangedMap = new HashMap<>();
        variableUsedMap = new HashMap<>();
        internalVar = null;

        addGlobalVariable(serviceInfo);
        addParamVariable(operation);
    }
    private void addGlobalVariable(ServiceInfo serviceInfo){
        serviceInfo.getGlobalVariableList()
                .forEach(this::addVariable);
    }
    private void addParamVariable(Operation operation){
        operation.getInputParamList()
                .forEach(this::addVariable);
        Variable returnVar = new Variable("result", operation.getReturnType(), Variable.ScopeType.RETURN);
        addVariable(returnVar);
    }

    public Variable addInternalVar(EntityInfo entityInfo){
        String name = "just@joke";
        Variable internal = new Variable(name, new EntityTypeInfo(entityInfo), Variable.ScopeType.INTERNAL);
        addVariable(internal);
        return internal;
    }
    public void removeInternalVar() {
        variableMap.remove(internalVar.getName());
        internalVar = null;
    }

    public void addVariable(Variable variable) {
        if(variable.getScopeType() == Variable.ScopeType.INTERNAL){
            if(this.internalVar != null){
                throw new UnsupportedOperationException();
            }
            this.internalVar = variable;
        }
        variableMap.put(variable.getName(), variable);
    }

    private void attributeChanged(Variable variable, String attribute){
        variableChangedMap.putIfAbsent(variable, new ArrayList<>());
        variableChangedMap.get(variable).add(attribute);
    }

    private void attributeUsed(Variable variable, String attribute){
        variableUsedMap.putIfAbsent(variable, new ArrayList<>());
        variableUsedMap.get(variable).add(attribute);
    }

    public Variable getVariable(String name){
        return variableMap.get(name);
    }

    private List<String> getVariableChanged(Variable variable){
        return variableChangedMap.get(variable);
    }
    private List<String> getVariableUsed(Variable variable){
        return variableUsedMap.get(variable);
    }



}

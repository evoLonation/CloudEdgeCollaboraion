package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeRef;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.StandardOp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.UnaryValue;
import net.mydreamy.requirementmodel.rEMODEL.*;

import java.util.ArrayList;
import java.util.List;

public class CallExpDealer extends FactoryContext {


    public CallExpDealer(InitPack initPack) {
        super(initPack);
    }

    public static class PCGlobalVariable extends PCRet{
        public Variable variable;
        public PCGlobalVariable(Variable variable) {
            this.variable = variable;
        }
    }
    public static class PCAttribute extends PCRet{
        public AttributeRef attributeRef;
        public PCAttribute(AttributeRef attributeRef) {
            this.attributeRef = attributeRef;
        }
    }
    public static class PCAssociation extends PCRet{
        public Variable variable;
        public EntityInfo.Association association;
        public PCAssociation(Variable variable, EntityInfo.Association association) {
            this.variable =variable;
            this.association = association;
        }
    }
    public abstract static class PCRet{ }

    // 变量的属性，直接转化为value
    // 变量的关联，先转化为select
    public PCRet dealPropertyCall(PropertyCallExpCS propertyCallExp){
        // 被呼叫者
        Variable callee;
        String attrStr;

        if(propertyCallExp.getName().getSymbol().equals("self")){
            if(propertyCallExp.getSelfproperty() == null) {
                callee = variableTable.getVariable(propertyCallExp.getAttribute());
                check(callee.getScopeType() == Variable.ScopeType.GLOBAL);
                return new PCGlobalVariable(callee);
            }else{
                callee = variableTable.getVariable(propertyCallExp.getSelfproperty().getSymbol());
                check(callee.getScopeType() == Variable.ScopeType.GLOBAL);
                attrStr = propertyCallExp.getAttribute();
            }
        }else{
            if(propertyCallExp.getSelfproperty() == null) {
                callee = variableTable.getVariable(propertyCallExp.getName().getSymbol());
                attrStr = propertyCallExp.getAttribute();
            } else {
                throw new UnsupportedOperationException();
            }
        }
        EntityInfo entityInfo = ((EntityTypeInfo) callee.getType()).getEntityInfo();

        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
        // 例如：patient.Temperatures_timestamp_10，指按timestamp降序取前10个
        String[] subString = attrStr.split("_");
        String key = subString[0];

        EntityInfo.KeyType keyType = entityInfo.getKeyType(key);
        if(keyType == EntityInfo.KeyType.ATTRIBUTE){
            check(subString.length == 1);
            return new PCAttribute(new AttributeRef(callee, entityInfo.getAttribute(key)));
        }else if(keyType == EntityInfo.KeyType.ASSOCIATION){
            EntityInfo.Attribute sortAttr = null;
            Integer limit = null;
            if(subString.length >= 2){
                sortAttr = entityInfo.getAssociation(key).getTargetEntity().getAttribute(subString[1]);
                if(subString.length == 3) {
                    limit = Integer.parseInt(subString[2]);
                }else{
                    throw new UnsupportedOperationException();
                }
            }
            return new PCAssociation(callee, entityInfo.getAssociation(key));
        } else {
            throw new UnsupportedOperationException();
        }
    }


    // 判断entityInfo
    // 只能是allInstance()
    public EntityInfo dealClassiferCall(ClassiferCallExpCS classiferCallExp){
        check(classiferCallExp.getOp().equals("allInstance()"));
        return entityInfoMap.get(classiferCallExp.getEntity());
    }

    // todo
    public void dealStandardCall(StandardNavigationCallExpCS callExp){
        throw new UnsupportedOperationException();
    }

    public static abstract class SORes {

    }
    public static class SOStandardOP extends SORes {
        public StandardOp standardOp;
        public SOStandardOP(StandardOp standardOp) {
            this.standardOp = standardOp;
        }
    }
    public static class SOIsUndefined extends SORes {
        public Variable variable;

        public SOIsUndefined(Variable variable) {
            this.variable = variable;
        }
    }
    public SORes dealStandardOperation(StandardOperationExpCS operationExp){
        var op = operationExp.getPredefinedop().getName();
        switch (op){
        case "oclIsUndefined()":
            Variable variable;
            if(operationExp.getProperty() == null){
                variable = variableTable.getVariable(operationExp.getObject().getSymbol());
            }else{
                check(operationExp.getObject().getSymbol().equals("self"));
                variable = variableTable.getVariable(operationExp.getProperty().getSymbol());
            }
            variable.mustGetEntity();
            check(!variable.getType().isMulti());
            return new SOIsUndefined(variable);
        default:
            throw new UnsupportedOperationException();
        }
    }
    public abstract class OCRes{

    }
    public class OCGenerateId extends OCRes {

    }
    public class OCThirdParty extends  OCRes{
        public String operationName;
        public List<UnaryValue> unaryValueList;

        public OCThirdParty(String operationName, List<UnaryValue> unaryValueList) {
            this.operationName = operationName;
            this.unaryValueList = unaryValueList;
        }
    }
    public OCRes dealOperationCall(OperationCallExpCS operationCallExpCS){
        var name = operationCallExpCS.getName();
        if(name.equals("generateId")){
            return new OCGenerateId();
        }else {
            var params = operationCallExpCS.getParameters();
            List<UnaryValue> paramsList = new ArrayList<>();
            params.forEach(param -> {
                if(param.getObjectproperty() != null){
                    var ret = (PCAttribute)dealPropertyCall(param.getObjectproperty());
                    paramsList.add(ret.attributeRef);
                }
            });
            return new OCThirdParty(name, paramsList);
        }
    }
}

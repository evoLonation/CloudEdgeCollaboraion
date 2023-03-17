package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Limit;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Sort;
import net.mydreamy.requirementmodel.rEMODEL.ClassiferCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.PropertyCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.StandardNavigationCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.StandardOperationExpCS;

public class CallExpDealer extends OperationBodyContext{


    public static class PCGlobalVariable extends PCRet{
        public PCGlobalVariable(Variable variable) {
            super(variable);
        }
    }
    public static class PCAttribute extends PCRet{
        public EntityInfo.Attribute attribute;

        public PCAttribute(Variable variable, EntityInfo.Attribute attribute) {
            super(variable);
            this.attribute = attribute;
        }
    }
    public static class PCAssociation extends PCRet{
        public EntityInfo.Association association;
        public Sort sort;
        public Limit limit;

        public PCAssociation(Variable variable, EntityInfo.Association association, Sort sort, Limit limit) {
            super(variable);
            this.association = association;
            this.sort = sort;
            this.limit = limit;
        }
    }
    public abstract static class PCRet{
        public Variable variable;

        public PCRet(Variable variable) {
            this.variable = variable;
        }
    }

    // 变量的属性，直接转化为value
    // 变量的关联，先转化为select
    public PCRet dealPropertyCall(PropertyCallExpCS propertyCallExp){
        // 被呼叫者
        Variable callee;
        String attrStr;

        if(propertyCallExp.getName().getSymbol().equals("self")){
            if(propertyCallExp.getSelfproperty() == null) {
                callee = variableTable.getVariable(propertyCallExp.getAttribute());
                check(variableTable.getVariableType(callee) == VariableTable.VariableType.GLOBAL);
                return new PCGlobalVariable(callee);
            }else{
                callee = variableTable.getVariable(propertyCallExp.getSelfproperty().getSymbol());
                check(variableTable.getVariableType(callee) == VariableTable.VariableType.GLOBAL);
                attrStr = propertyCallExp.getAttribute();
            }
        }else{
            if(propertyCallExp.getSelfproperty() == null) {
                callee = variableTable.getVariable(propertyCallExp.getName().getSymbol());
                attrStr = propertyCallExp.getAttribute();
            }else{
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
            return new PCAttribute(callee, entityInfo.getAttribute(key));
        }else if(keyType == EntityInfo.KeyType.ASSOCIATION){
            Sort sort = null;
            Limit limit = null;
            if(subString.length >= 2){
                sort = new Sort(subString[1], Sort.Rule.DESCENDING);
                if(subString.length == 3){
                    limit = new Limit(Integer.parseInt(subString[2]));
                }else{
                    throw new UnsupportedOperationException();
                }
            }
            return new PCAssociation(callee, entityInfo.getAssociation(key), sort, limit);
        }else{
            throw new UnsupportedOperationException();
        }


//        String key = propertyCallExp.getAttribute();
//        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
//        // 例如：patient.Temperatures_timestamp_10，指按timestamp降序取前10个
//        String[] subKeys = key.split("_");
//        String realKey = subKeys[0];
//        switch (entityInfo.getKeyType(realKey)){
//            case ATTRIBUTE:
//                EntityInfo.Attribute attribute = entityInfo.getAttribute(key);
//                return new DefinitionFactory.PCValue(new AttributeValue(callee, attribute.getName()));
//            case ASSOCIATION:
//                EntityInfo.Association association = entityInfo.getAssociation(key);
//
//                SelectBuilder selectBuilder = new SelectBuilder();
//                selectBuilder.setLocation(serviceInfo.getLocation());
//
//                if(association instanceof EntityInfo.ForeignKeyAss){
//                    EntityInfo.ForeignKeyAss foreignKeyAss = (EntityInfo.ForeignKeyAss) association;
//                    EntityInfo targetEntity = foreignKeyAss.getTargetEntity();
//                    Condition condition;
//                    if(foreignKeyAss.isMulti()){
//                        // 外键字段在对方
//                        selectBuilder.addCondition(
//                                new AtomicCondition(
//                                        foreignKeyAss.getRefAttrName(),
//                                        new AttributeValue(callee, entityInfo.getIdAttribute().getName()),
//                                        AtomicCondition.OP.EQUALS
//                                )
//                        );
//                        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
//                        if(subKeys.length >= 2){
//                            selectBuilder.addSort(new Sort(subKeys[1], Sort.Rule.DESCENDING));
//                            if(subKeys.length >= 3){
//                                selectBuilder.setLimit(Integer.parseInt(subKeys[2]));
//                            }
//                        }
//
//                    }else{
//                        // 外键字段在己方
//                        selectBuilder.addCondition(
//                                new AtomicCondition(
//                                        targetEntity.getIdAttribute().getName(),
//                                        new AttributeValue(callee, foreignKeyAss.getName()),
//                                        AtomicCondition.OP.EQUALS
//                                )
//                        );
//                        // todo 外键字段在对方?
//                    }
//                }else {
//                    // todo 多-多关联
//                    throw new UnsupportedOperationException();
//                }
//                return new DefinitionFactory.PCSelect(selectBuilder, association.getTargetEntity());
//        }
//        throw new UnsupportedOperationException();
    }


    // 判断entityInfo
    // 只能是allInstance()
    public EntityInfo dealClassiferCall(ClassiferCallExpCS classiferCallExp){
        SelectBuilder selectBuilder = new SelectBuilder();
        selectBuilder.setLocation(serviceInfo.getLocation());
        check(classiferCallExp.getOp().equals("allInstance()"));
        return entityInfoMap.get(classiferCallExp.getEntity());
    }


    // todo
    public void dealStandardCall(StandardNavigationCallExpCS callExp){

    }

    public static abstract class SORes {

    }
    public static class SORValue extends SORes {
        public Value value;

        public SORValue(Value value) {
            this.value = value;
        }
    }
    public static class SOIsNew extends SORes { }
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
            check(variable.getType() instanceof EntityTypeInfo);
            check(!variable.getType().isMulti());
            return new SORValue(new IsUndefined(variable));
        default:
            throw new UnsupportedOperationException();
        }
    }
}

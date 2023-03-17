package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AtomicCondition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Select;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Sort;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeValue;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.VariableValue;
import net.mydreamy.requirementmodel.rEMODEL.*;
import net.mydreamy.requirementmodel.rEMODEL.impl.EntityTypeImpl;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;

public class DefinitionFactory extends OperationBodyContext{
    private Definition definition;

    public DefinitionFactory(Definition definition) {
        this.definition = definition;
    }

    public void build(){

    }

    private void dealVariable(VariableDeclarationCS variableDeclaration){
        //有两种情况：
        // 第一种是select

        // 第二种是setter
    }

    private void dealAtomicExpression(AtomicExpression atomicExpression) {

    }



    public static class IESelect{
        // 待设置variable
        public SelectBuilder selectBuilder;
        public EntityInfo entityInfo;

        public IESelect(SelectBuilder selectBuilder, EntityInfo entityInfo) {
            this.selectBuilder = selectBuilder;
            this.entityInfo = entityInfo;
        }
    }
    // 处理集合的操作
    // 一个是转化为数据库的condition
    // todo 一个是对已有的集合执行操作
    private IESelect dealIteratorExp(IteratorExpCS iteratorExp){
        if(iteratorExp.getSimpleCall() != null){
            // todo
            throw new UnsupportedOperationException();
        }else if(iteratorExp.getObjectCall() != null){
            FeatureCallExpCS featureCallExpCS = iteratorExp.getObjectCall();
            SelectBuilder selectBuilder;
            EntityInfo entityInfo;
            if(featureCallExpCS instanceof PropertyCallExpCS){
                var ret = (PCSelect) dealPropertyCall((PropertyCallExpCS) featureCallExpCS);
                selectBuilder = ret.selectBuilder;
                entityInfo = ret.entityInfo;
            } else if(featureCallExpCS instanceof ClassiferCallExpCS) {
                var ret = dealClassiferCall((ClassiferCallExpCS) featureCallExpCS);
                selectBuilder = ret.selectBuilder;
                entityInfo = ret.entityInfo;
            }
            switch (iteratorExp.getIterator()){
            case "any()":

            case "select()":
            case "collect()":



            }

        }else{
            throw new UnsupportedOperationException();
        }
    }




    public static class PCValue extends PCRet{
        public AttributeValue attributeValue;
        public PCValue(AttributeValue attributeValue) {
            this.attributeValue = attributeValue;
        }
    }
    public static class PCSelect extends PCRet{
        // 待设置variable
        public SelectBuilder selectBuilder;
        public EntityInfo entityInfo;

        public PCSelect(SelectBuilder selectBuilder, EntityInfo entityInfo) {
            this.selectBuilder = selectBuilder;
            this.entityInfo = entityInfo;
        }
    }
    public abstract static class PCRet{ }

    // 变量的属性，直接转化为value
    // 变量的关联，先转化为select
    private PCRet dealPropertyCall(PropertyCallExpCS propertyCallExp){
        // 被呼叫者
        Variable callee;
        if(propertyCallExp.getSelfproperty() == null){
            callee = getVariable(propertyCallExp.getName());
        }else{
            if(propertyCallExp.getName().getSymbol().equals("self")){
                callee = getVariable(propertyCallExp.getSelfproperty());
            }else{
                // todo
                throw new UnsupportedOperationException();
            }
        }
        EntityInfo entityInfo = ((EntityTypeInfo)callee.getType()).getEntityInfo();
        String key = propertyCallExp.getAttribute();
        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
        // 例如：patient.Temperatures_timestamp_10，指按timestamp降序取前10个
        String[] subKeys = key.split("_");
        String realKey = subKeys[0];
        switch (entityInfo.getKeyType(realKey)){
        case ATTRIBUTE:
            EntityInfo.Attribute attribute = entityInfo.getAttribute(key);
            return new PCValue(new AttributeValue(callee, attribute.getName()));
        case ASSOCIATION:
            EntityInfo.Association association = entityInfo.getAssociation(key);

            SelectBuilder selectBuilder = new SelectBuilder();
            selectBuilder.setLocation(serviceInfo.getLocation());

            if(association instanceof EntityInfo.ForeignKeyAss){
                EntityInfo.ForeignKeyAss foreignKeyAss = (EntityInfo.ForeignKeyAss) association;
                EntityInfo targetEntity = foreignKeyAss.getTargetEntity();
                Condition condition;
                if(foreignKeyAss.isMulti()){
                    // 外键字段在对方
                    selectBuilder.addCondition(
                            new AtomicCondition(
                                    foreignKeyAss.getRefAttrName(),
                                    new AttributeValue(callee, entityInfo.getIdAttribute().getName()),
                                    AtomicCondition.OP.EQUALS
                            )
                    );
                    // todo 无奈之举，通过key后面加下划线来模拟subList和sort
                    if(subKeys.length >= 2){
                        selectBuilder.addSort(new Sort(subKeys[1], Sort.Rule.DESCENDING));
                        if(subKeys.length >= 3){
                            selectBuilder.setLimit(Integer.parseInt(subKeys[2]));
                        }
                    }

                }else{
                    // 外键字段在己方
                    selectBuilder.addCondition(
                            new AtomicCondition(
                                    targetEntity.getIdAttribute().getName(),
                                    new AttributeValue(callee, foreignKeyAss.getName()),
                                    AtomicCondition.OP.EQUALS
                            )
                    );
                    // todo 外键字段在对方?
                }
            }else {
                // todo 多-多关联
                throw new UnsupportedOperationException();
            }
            return new PCSelect(selectBuilder, association.getTargetEntity());
        }
        throw new UnsupportedOperationException();
    }


    public static class CCSelect{
        public SelectBuilder selectBuilder;
        public EntityInfo entityInfo;

        public CCSelect(SelectBuilder selectBuilder, EntityInfo entityInfo) {
            this.selectBuilder = selectBuilder;
            this.entityInfo = entityInfo;
        }
    }

    // 判断entityInfo
    // 只能是allInstance()
    private CCSelect dealClassiferCall(ClassiferCallExpCS classiferCallExp){
        SelectBuilder selectBuilder = new SelectBuilder();
        selectBuilder.setLocation(serviceInfo.getLocation());
        check(classiferCallExp.getOp().equals("allInstance()"));
        return new CCSelect(selectBuilder, entityInfoMap.get(classiferCallExp.getEntity()));
    }


    private void dealCallExp() {
        // propertyCallExpCS
        // 调用变量的属性
        // 调用变量的关联
        // classiferCallExpCS
        // 调用类的静态allInstance()
    }





    private Select getSelect(VariableDeclarationCS variableDeclaration){
        // 定义variable
        String name = variableDeclaration.getName();
        Type type = getType(variableDeclaration.getType());
        Variable variable = new Variable(name, type);
        variableTable.addDefinitionVariable(variable);
        // 得到condition
        LogicFormulaExpCS initExp = (LogicFormulaExpCS) variableDeclaration.getInitExpression();
        Condition condition = getCondition(initExp, (EntityInfo) variable.getType());
        return new Select(variable, condition, new ArrayList<>(), null, null);
    }

    private Type getType(TypeCS typeCS){
        if(typeCS instanceof EntityType){
            EntityType entityType = (EntityType) typeCS;
            return entityInfoMap.get(entityType.getEntity().getName());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    private Condition getCondition(LogicFormulaExpCS initExpression, EntityInfo entityInfo){
        // logic的exps只能有一个
        check(initExpression.getAtomicexp().size() == 1);
        // 子exp只能是atomicExp
        AtomicExpression atomicExpression = (AtomicExpression) initExpression.getAtomicexp().get(0);
        // 只有left
        check(atomicExpression.getRightside() == null);

        IteratorExpCS iteratorExpCS = (IteratorExpCS) atomicExpression.getLeftside();
        ClassiferCallExpCS classiferCallExpCs = (ClassiferCallExpCS) iteratorExpCS.getObjectCall();
        check(classiferCallExpCs.getEntity().equals(entityInfo.getName()));
        check(classiferCallExpCs.getOp().equals("allInstance()"));
        if(iteratorExpCS.getVaribles().size() == 1){

            // 处理初始化变量
            VariableDeclarationCS variableDeclaration = iteratorExpCS.getVaribles().get(0);
            String varName = variableDeclaration.getName();
            EntityType varType = (EntityType) variableDeclaration.getType();
            check(varType.getEntity().getName().equals(entityInfo.getName()));
            // 处理表达式
            // var.attr = ?
            LogicFormulaExpCS allExp = (LogicFormulaExpCS) iteratorExpCS.getExp();
            check(allExp.getAtomicexp().size() == 1);
            AtomicExpression judgeExp = (AtomicExpression) allExp.getAtomicexp().get(0);
            // 左边是 var.attr
            PropertyCallExpCS left = (PropertyCallExpCS) judgeExp.getLeftside();
            check(left.getName().getSymbol().equals(varName));
            check(judgeExp.getInfixop().equals("="));
            String attr = left.getAttribute();
            Value value = getValue(judgeExp.getRightside());
            return new AttributeEquals(attr, value);
        } else {
            throw new UnsupportedOperationException();
        }
    }


    private Value getValue(EObject exp) {
        if(exp instanceof PropertyCallExpCS) {
            PropertyCallExpCS property = (PropertyCallExpCS) exp;
            if(property.getName().getSymbol().equals("self")){
                return new AttributeValue(variableTable.getGlobalVariable(property.getSelfproperty().getSymbol()), property.getAttribute());
            }else{
                return new AttributeValue(variableTable.getLocalVariable(property.getName().getSymbol()), property.getAttribute());
            }
        }else if(exp instanceof VariableExpCS){
            return new VariableValue(variableTable.getLocalVariable(((VariableExpCS) exp).getSymbol()));
        }else if(exp instanceof LogicFormulaExpCS){
            LogicFormulaExpCS lf = (LogicFormulaExpCS) exp;
            if(lf.getAtomicexp().size() == 1){
                return getValue(lf.getAtomicexp().get(0));
            }else{
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }


}

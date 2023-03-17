package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import net.mydreamy.requirementmodel.rEMODEL.*;

import java.util.List;

// iteratorExp通常用于表达集合的操作
public class IteratorExpDealer extends OperationBodyContext{
    private final CallExpDealer callExpDealer = new CallExpDealer();


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
    public IESelect dealIteratorExp(IteratorExpCS iteratorExp){
        boolean isAllInstance;
        EntityInfo entityInfo = null;
        if(iteratorExp.getSimpleCall() != null){
            // todo
            throw new UnsupportedOperationException();
        }else if(iteratorExp.getObjectCall() != null){
            var callExp = iteratorExp.getObjectCall();
            if(callExp instanceof PropertyCallExpCS){
                // todo
                throw new UnsupportedOperationException();
            } else if(callExp instanceof ClassiferCallExpCS) {
                entityInfo = callExpDealer.dealClassiferCall((ClassiferCallExpCS) callExp);
                isAllInstance = true;
            }
        }else{
            throw new UnsupportedOperationException();
        }
        String internalVar = dealVariables(iteratorExp.getVaribles(), entityInfo);
        switch (iteratorExp.getIterator()) {
            case "any()":

            case "select()":
            case "collect()":



        }
    }

    private void dealVariables(List<VariableDeclarationCS> variableList, EntityInfo entityInfo) {
        check(variableList.size() == 1);
        var variable = variableList.get(0);
        var varName = variable.getName();
        var varType = getType(variable.getType());
        check(new EntityTypeInfo(entityInfo).equals(varType));
        variableTable.addInternalVar(new Variable(varName, varType));
    }

    private Condition dealAnySelect(LogicFormulaExpCS logicFormulaExp, EntityInfo entityInfo) {
        // left
        check(logicFormulaExp.getAtomicexp().size() == 1);
        var atomicExp = (AtomicExpression) logicFormulaExp.getAtomicexp().get(0);
        check(atomicExp.getLeftside() instanceof PropertyCallExpCS);
        var left = (PropertyCallExpCS) atomicExp.getLeftside();
        check(left.getSelfproperty() == null);
        check(left.getName().getSymbol().equals(variable));
        var attribute = left.getAttribute();
        check(entityInfo.getKeyType(attribute) == EntityInfo.KeyType.ATTRIBUTE);
        // op
        var opStr = atomicExp.getOp();
        AtomicCondition.OP op;
        switch (opStr){
            case "=": op = AtomicCondition.OP.EQUALS; break;
            // todo
            default: throw new UnsupportedOperationException();
        }
        // right
        var right = atomicExp.getRightside();

        // todo exp (right的right）

    }

}

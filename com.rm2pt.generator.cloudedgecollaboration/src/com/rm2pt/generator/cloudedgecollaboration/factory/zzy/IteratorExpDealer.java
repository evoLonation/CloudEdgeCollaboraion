package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AtomicCondition;
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
    private DefinitionFactory.IESelect dealIteratorExp(IteratorExpCS iteratorExp){
        if(iteratorExp.getSimpleCall() != null){
            // todo
            throw new UnsupportedOperationException();
        }else if(iteratorExp.getObjectCall() != null){
            FeatureCallExpCS featureCallExpCS = iteratorExp.getObjectCall();
            SelectBuilder selectBuilder;
            EntityInfo entityInfo = null;
            if(featureCallExpCS instanceof PropertyCallExpCS){
                var ret = (DefinitionFactory.PCSelect) callExpDealer.dealPropertyCall((PropertyCallExpCS) featureCallExpCS);
                selectBuilder = ret.selectBuilder;
                entityInfo = ret.entityInfo;
            } else if(featureCallExpCS instanceof ClassiferCallExpCS) {
                var ret = callExpDealer.dealClassiferCall((ClassiferCallExpCS) featureCallExpCS);
                selectBuilder = ret.selectBuilder;
                entityInfo = ret.entityInfo;
            }
            String internalVar = dealVariables(iteratorExp.getVaribles(), entityInfo);
            switch (iteratorExp.getIterator()) {
                case "any()":

                case "select()":
                case "collect()":



            }

        }else{
            throw new UnsupportedOperationException();
        }
    }

    private String dealVariables(List<VariableDeclarationCS> variableList, EntityInfo entityInfo) {
        check(variableList.size() == 1);
        var variable = variableList.get(0);
        check(new EntityTypeInfo(entityInfo).equals(getType(variable.getType())));
        return variable.getName();
    }

    private Condition dealAnySelect(LogicFormulaExpCS logicFormulaExp, String variable, EntityInfo entityInfo) {
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

    private Condition dealAtomicExp(AtomicExpression atomicExp) {

    }

}

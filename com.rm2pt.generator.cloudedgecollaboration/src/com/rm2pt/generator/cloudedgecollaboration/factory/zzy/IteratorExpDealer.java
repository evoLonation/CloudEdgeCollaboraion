package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ExpType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.*;
import net.mydreamy.requirementmodel.rEMODEL.*;

import java.util.List;

// iteratorExp通常用于表达集合的操作
public class IteratorExpDealer extends FactoryContext {
    private final CallExpDealer callExpDealer;
    private final AssociationDealer associationDealer;
    private final LogicExpDealer logicExpDealer;

    public IteratorExpDealer(InitPack initPack, CallExpDealer callExpDealer, AssociationDealer associationDealer, LogicExpDealer logicExpDealer) {
        super(initPack);
        this.callExpDealer = callExpDealer;
        this.associationDealer = associationDealer;
        this.logicExpDealer = logicExpDealer;
    }

    // 处理集合的操作
    // 一个是转化为数据库的condition
    // 一个是对已有的集合执行操作
    public void dealIteratorExp(IteratorExpCS iteratorExp, Variable targetVar) {
        // 得到source
        Source source;
        if(iteratorExp.getSimpleCall() != null) {
            source = new Collection(variableTable.getVariable(iteratorExp.getSimpleCall()));
        } else if(iteratorExp.getObjectCall() != null) {
            var callExp = iteratorExp.getObjectCall();
            if(callExp instanceof PropertyCallExpCS) {
                // 必须是多关联
                var ret = (CallExpDealer.PCAssociation)callExpDealer.dealPropertyCall((PropertyCallExpCS) callExp);
                check(ret.association.isMulti());
                var tempVar = variableTable.getTempVariable(ret.variable, ret.association);
                // 先生成关联的获取操作
                associationDealer.dealMultiAssGet(tempVar, ret.variable, ret.association);
                source = new Collection(tempVar);
            } else if(callExp instanceof ClassiferCallExpCS) {
                var entityInfo = callExpDealer.dealClassiferCall((ClassiferCallExpCS) callExp);
                source = new AllInstance(entityInfo);
            } else{
                throw new UnsupportedOperationException();
            }
        }else{
            throw new UnsupportedOperationException();
        }

        var internalVar = getInternalVar(iteratorExp.getVaribles(), source.getEntityInfo());
        var isMulti = true;
        switch (iteratorExp.getIterator()) {
        case "any":
            isMulti = false;
        case "select":
            var condition = new Condition(targetVar, source, internalVar, logicExpDealer.dealOCLExp(iteratorExp.getExp(), ExpType.COLLECTION), isMulti);
            addStatement(condition);
            break;
        case "collect":
            // todo
        default: throw new UnsupportedOperationException();
        }
        variableTable.removeInternalVar();
    }

    private Variable getInternalVar(List<VariableDeclarationCS> variableList, EntityInfo entityInfo) {
        check(variableList.size() == 1);
        var variable = variableList.get(0);
        var varName = variable.getName();
        var varType = getType(variable.getType());
        check(new EntityTypeInfo(entityInfo).equals(varType));
        var internal = new Variable(varName, varType, Variable.ScopeType.INTERNAL);
        variableTable.addVariable(internal);
        return internal;
    }

}

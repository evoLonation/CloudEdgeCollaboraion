package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.AtomicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ExpType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AllInstance;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Assign;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeRef;

// 用于处理关联的引用、赋值等等
public class AssociationDealer extends OperationBodyContext{

    public void dealSingleAssAssign(Variable variable, EntityInfo.ForeignKeyAss association, Variable sourceVar) {
        checkAssign(sourceVar, variable, association, false);
        var entityInfo = variable.mustGetEntity();
        var sourceEntity = sourceVar.mustGetEntity();
        // 如果单关联是自己的外键
        var assign = new Assign(
                new AttributeRef(variable, entityInfo.getAttribute((association).getRefAttrName())),
                new AttributeRef(sourceVar, sourceEntity.getIdAttribute()));
        operationBody.addStatement(assign);
        // todo 如果单关联是对方的unique外键
    }

    public void dealSingleAssRemove(Variable variable, EntityInfo.Association association){
        // todo 是不是应该将golang的对应外键设置为指针类型？ 还是直接0值就行
        throw new UnsupportedOperationException();
    }

    public void dealAssGet(Variable variable, Variable sourceVar, EntityInfo.Association sourceAss){
        if(sourceAss.isMulti()){
            dealMultiAssGet(variable, sourceVar, sourceAss);
        }else{
            dealSingleAssGet(variable, sourceVar, (EntityInfo.ForeignKeyAss) sourceAss);
        }
    }

    public void dealSingleAssGet(Variable variable, Variable sourceVar, EntityInfo.ForeignKeyAss sourceAss) {
        checkAssign(variable, sourceVar, sourceAss, false);
        var sourceEntity = sourceAss.getTargetEntity();
        var targetEntity = variable.mustGetEntity();
        check(sourceEntity.equals(targetEntity));
        var internal = variableTable.addInternalVar(targetEntity);
        var exp = new AtomicExp(
                new AttributeRef(internal, targetEntity.getIdAttribute()),
                new AttributeRef(sourceVar, sourceVar.mustGetEntity().getAttribute(sourceAss.getRefAttrName())),
                AtomicExp.OP.EQ,
                ExpType.COLLECTION);
        var condition = new Condition(variable, new AllInstance(targetEntity), internal, exp, false);
        variableTable.removeInternalVar();
        operationBody.addStatement(condition);
    }

    public void dealMultiAssGet(Variable variable, Variable sourceVar, EntityInfo.Association sourceAss) {
        checkAssign(variable, sourceVar, sourceAss, true);
        var sourceEntity = sourceAss.getTargetEntity();
        var targetEntity = variable.mustGetEntity();
        check(sourceEntity.equals(targetEntity));
        if(sourceAss instanceof EntityInfo.ForeignKeyAss){
            var internal = variableTable.addInternalVar(targetEntity);
            var exp = new AtomicExp(
                    new AttributeRef(internal, targetEntity.getAttribute(((EntityInfo.ForeignKeyAss) sourceAss).getRefAttrName())),
                    new AttributeRef(sourceVar, sourceVar.mustGetEntity().getIdAttribute()),
                    AtomicExp.OP.EQ,
                    ExpType.COLLECTION);
            var condition = new Condition(variable, new AllInstance(targetEntity), internal, exp, true);
            variableTable.removeInternalVar();
            operationBody.addStatement(condition);
        }else{
            // todo join table
            throw new UnsupportedOperationException();
        }
    }

    private void checkAssign(Variable variable, Variable sourceVar, EntityInfo.Association sourceAss, boolean isMulti){
        check(sourceAss.isMulti() == isMulti);
        checkVariable(variable, sourceAss);
        checkCorrespond(sourceVar, sourceAss);
    }

    private EntityInfo checkCorrespond(Variable variable, EntityInfo.Association association){
        check(!variable.getType().isMulti());
        check(variable.mustGetEntity().getKeyType(association.getName()) == EntityInfo.KeyType.ASSOCIATION);
        return ((EntityTypeInfo)variable.getType()).getEntityInfo();
    }

    private void checkVariable(Variable variable, EntityInfo.Association association){
        check(variable.getType().isMulti() == association.isMulti());
        check(variable.mustGetEntity().equals(association.getTargetEntity()));
    }
}

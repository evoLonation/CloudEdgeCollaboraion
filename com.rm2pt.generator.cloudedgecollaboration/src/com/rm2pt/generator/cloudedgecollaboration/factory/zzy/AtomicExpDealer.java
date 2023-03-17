package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.AtomicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.*;
import net.mydreamy.requirementmodel.rEMODEL.*;
import org.eclipse.emf.ecore.EObject;

public class AtomicExpDealer extends OperationBodyContext {

    private final CallExpDealer callExpDealer = new CallExpDealer();


    // 需要设置对方的外键的情况
    public static class OppositeRef extends LeftRet{
        // 对方的外键 = idAttributeValue
        private AttributeRef idAttributeRef;
        public OppositeRef(AttributeRef idAttributeRef) {
            this.idAttributeRef = idAttributeRef;
        }
    }
    // 返回左值的情况
    public static class LValueRet extends LeftRet{
        private LValue lValue;
        public LValueRet(LValue lValue) {
            this.lValue = lValue;
        }
    }
    public abstract static class LeftRet{ }

    // 处理 left = right1 op right2 的 left
    // 3种情况
    // globalVariable或者return本身
    // 变量的属性
    // 变量的单关联（可能需要为对方赋值）
    // 有些赋值无法转换为LValue = Value的形式
    public LeftRet dealLeftExp(LeftSubAtomicExpression exp){
        if(exp instanceof VariableExpCS){
            String varName = ((VariableExpCS) exp).getSymbol();
            var variable = variableTable.getVariable(varName);
            var varType = variableTable.getVariableType(variable);
            check(varType == VariableTable.VariableType.GLOBAL || varType == VariableTable.VariableType.RETURN);
            return new LValueRet(new VariableValue(variable));
        }else if(exp instanceof PropertyCallExpCS){
            var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) exp);
            var variable = ret.variable;
            if(ret instanceof CallExpDealer.PCAttribute){
                return new LValueRet(new AttributeRef(variable, ((CallExpDealer.PCAttribute) ret).attribute));
            }else if(ret instanceof CallExpDealer.PCAssociation){
                var asso = ((EntityInfo.ForeignKeyAss)((CallExpDealer.PCAssociation) ret).association);
                check(!asso.isMulti());
                // todo 设置对方的外键的情况
                return new LValueRet(new AttributeRef(variable, (((EntityTypeInfo)variable.getType()).getEntityInfo()).getAttribute(asso.getRefAttrName())));
            }else if(ret instanceof CallExpDealer.PCGlobalVariable){
                return new LValueRet(new VariableValue(variable));
            }else {
                throw new UnsupportedOperationException();
            }
        }else {
            throw new UnsupportedOperationException();
        }
    }


    // 变量的单关联
    public static class SingleAssociationRet extends RightRet{
        public Variable variable;
        public EntityInfo.Association association;

        public SingleAssociationRet(Variable variable, EntityInfo.Association association) {
            this.variable = variable;
            this.association = association;
        }
    }
    // 变量的属性
    // 变量的多重关联
    // 变量本身
    // 字面值
    public static class ValueRet extends RightRet{
        public RValue RValue;
        public ValueRet(RValue RValue) {
            this.RValue = RValue;
        }
    }
    public abstract static class RightRet{ }
    // 处理 left = right1 op right2 的 right1 或者 right2
    // 或者 a:Patient : exp 中的exp
    public RightRet dealRightExp(OCLExpressionCS exp){
        if(exp instanceof VariableExpCS) {
            String varName = ((VariableExpCS) exp).getSymbol();
            var variable = variableTable.getVariable(varName);
            return new ValueRet(new VariableValue(variable));
        }else if(exp instanceof PropertyCallExpCS){
            var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) exp);
            var variable = ret.variable;
            if(ret instanceof CallExpDealer.PCAttribute){
                return new ValueRet(new AttributeRef(variable, ((CallExpDealer.PCAttribute) ret).attribute));
            }else if(ret instanceof CallExpDealer.PCAssociation){
                var asso = ((CallExpDealer.PCAssociation) ret).association;
                if(asso.isMulti()){
                    if(asso instanceof EntityInfo.ForeignKeyAss){
                        EntityInfo targetEntity = asso.getTargetEntity();
                        Variable internalVar = variableTable.addInternalVar(targetEntity);
                        AtomicExp atomicExp = new AtomicExp(
                                new AttributeRef(
                                        internalVar,
                                        targetEntity.getAttribute(((EntityInfo.ForeignKeyAss) asso).getRefAttrName())
                                ),
                                new AttributeRef(
                                        variable,
                                        ((EntityTypeInfo)variable.getType()).getEntityInfo().getIdAttribute()),
                                AtomicExp.OP.EQ);
                        return new ValueRet(new CollectionOperation(new Condition(internalVar, atomicExp)));
                    }else if(asso instanceof EntityInfo.JoinTableAss){
                        // todo
                        throw new UnsupportedOperationException();
                    }else{
                        throw new UnsupportedOperationException();
                    }
                }else{
                    check(asso instanceof EntityInfo.ForeignKeyAss);
                    return new SingleAssociationRet(variable, asso);
                }
            }else if(ret instanceof CallExpDealer.PCGlobalVariable){
                return new ValueRet(new VariableValue(variable));
            }else {
                throw new UnsupportedOperationException();
            }
        }else if(exp instanceof LiteralExpCS){
            // todo
            throw new UnsupportedOperationException();
        }else if(exp instanceof StandardOperationExpCS){
            var ret = callExpDealer.dealStandardOperation((StandardOperationExpCS) exp);
            if(ret instanceof CallExpDealer.SORValue){
                return new ValueRet(((CallExpDealer.SORValue) ret).value);
            } else {
                // todo
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public RValue getRValue(EObject exp) {
         dealRightExp((OCLExpressionCS) exp);

    }

    public RValue getValue(OCLExpressionCS exp1, String op, EObject exp2){
        // todo
        throw new UnsupportedOperationException();
    }

    public AtomicExp getAtomicExp(AtomicExpression exp) {
        var left = exp.getLeftside();
        var right1 = exp.getRightside();
        EObject right2;
        if(exp.getExp() != null){
            right2 = exp.getExp().getLeftside();
        }else if(exp.getNum() != null) {
            right2 = exp.getNum();
        }else{
            right2 = null;
        }

    }

}

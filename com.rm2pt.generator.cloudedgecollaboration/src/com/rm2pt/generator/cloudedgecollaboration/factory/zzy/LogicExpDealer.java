package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.AtomicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ConnectExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ExpType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition.IsUndefinedExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.RValue;
import net.mydreamy.requirementmodel.rEMODEL.*;

import java.util.stream.Collectors;

// 用于处理precondition部分或者集合操作部分的逻辑判断
public class LogicExpDealer extends FactoryContext {

    private final CallExpDealer callExpDealer;
    private final RValueDealer rValueDealer;

    public LogicExpDealer(InitPack initPack, CallExpDealer callExpDealer, RValueDealer rValueDealer) {
        super(initPack);
        this.callExpDealer = callExpDealer;
        this.rValueDealer = rValueDealer;
    }

    public LogicExp dealOCLExp(OCLExpressionCS oclExpressionCS, ExpType type){
        if(oclExpressionCS instanceof LogicFormulaExpCS){
            return dealLogicFormulaExp((LogicFormulaExpCS) oclExpressionCS, type);
        } else if(oclExpressionCS instanceof AtomicExpression){
            return dealAtomicExp((AtomicExpression) oclExpressionCS, type);
        }
        throw new UnsupportedOperationException();
    }

    public LogicExp dealLogicFormulaExp(LogicFormulaExpCS logicFormulaExp, ExpType type) {
        // left
        var atomicExpList = logicFormulaExp.getAtomicexp().stream().map(e -> {
            if(e instanceof AtomicExpression){
                return dealAtomicExp((AtomicExpression) e, type);
            }else if(e instanceof NestedExpCS){
                var subexp = (LogicFormulaExpCS)((NestedExpCS) e).getNestedExpression();
                return dealLogicFormulaExp(subexp, type);
            }else{
                throw new UnsupportedOperationException();
            }
        }).collect(Collectors.toList());
        var opList = logicFormulaExp.getConnector().stream().map(op -> {
            switch (op){
                case "AND": return ConnectExp.OP.AND;
                case "OR" : return ConnectExp.OP.OR;
                default: throw new UnsupportedOperationException();
            }
        }).collect(Collectors.toList());
        return new ConnectExp(atomicExpList, opList, type);
    }

    private LogicExp dealAtomicExp(AtomicExpression atomicExp, ExpType type) {
        // 先看左边
        var left =  atomicExp.getLeftside();
        var haveInternal = false;
        // 处理oclIsUndefined
        if(left instanceof StandardOperationExpCS){
            check(type == ExpType.PRECONDITION);
            var ret = (CallExpDealer.SOIsUndefined)callExpDealer.dealStandardOperation((StandardOperationExpCS) left);
            var right = (BooleanLiteralExpCS)atomicExp.getRightside();
            check(atomicExp.getInfixop().equals("="));
            return new IsUndefinedExp(ret.variable, right.getSymbol().equals("true"));
        }
        var leftValue = rValueDealer.getUnaryValue(left);
//         寻找是否使用了internalVariable
//        if(leftValue instanceof AttributeRef){
//            if(((AttributeRef)leftValue).getVariable().getScopeType() == Variable.ScopeType.INTERNAL){
//                check(type == Type.COLLECTION);
//                haveInternal = true;
//            }
//        }
        // 再看右边
        RValue rightValue;
        if(atomicExp.getOp() == null){
            rightValue = rValueDealer.getUnaryValue(atomicExp.getRightside());
        }else{
            if(atomicExp.getExp() != null){
                rightValue = rValueDealer.getBinaryValue(atomicExp.getRightside(), atomicExp.getExp(), atomicExp.getOp());
            }else{
                rightValue = rValueDealer.getBinaryValue(atomicExp.getRightside(), atomicExp.getNum(), atomicExp.getOp());
            }
        }
        AtomicExp.OP op;
        switch (atomicExp.getInfixop()){
            case "=" : op = AtomicExp.OP.EQ; break;
            // todo
            default: throw new UnsupportedOperationException();
        }
        return new AtomicExp(leftValue, rightValue, op, type);
    }
}

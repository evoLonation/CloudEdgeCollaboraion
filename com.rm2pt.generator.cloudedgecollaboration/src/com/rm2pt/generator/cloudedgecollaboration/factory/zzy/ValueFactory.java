package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeValue;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.VariableValue;
import net.mydreamy.requirementmodel.rEMODEL.*;
import org.eclipse.emf.ecore.EObject;

public class ValueFactory extends OperationBodyContext {

    private final CallExpDealer callExpDealer = new CallExpDealer();

    public Value getValue(RightSubAtomicExpression exp){
        if(exp instanceof VariableExpCS){
            return new VariableValue(variableTable.getLocalVariable(((VariableExpCS) exp).getSymbol()));
        }else if(exp instanceof LiteralExpCS){
            // todo 基本类型的value的结构
        }else if(exp instanceof PropertyCallExpCS){
            callExpDealer.dealPropertyCall((PropertyCallExpCS) exp);
        }else {
            // todo
            throw new UnsupportedOperationException();
        }
    }

    private Value genericGetValue(RightSubAtomicExpression exp1, String op, EObject exp2) {
        // todo 目前没有四则运算的Value的表示
        throw new UnsupportedOperationException();
    }
    public Value getValue(RightSubAtomicExpression exp1, String op, AtomicExpression exp2){
        return genericGetValue(exp1, op, exp2);
    }
    private Value getValue(RightSubAtomicExpression exp1, String op, LiteralExpCS exp2){
        return genericGetValue(exp1, op, exp2);
    }
}

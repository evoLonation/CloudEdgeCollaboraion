package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.BasicVariable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.BinaryValue;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.UnaryValue;
import net.mydreamy.requirementmodel.rEMODEL.AtomicExpression;
import net.mydreamy.requirementmodel.rEMODEL.LiteralExpCS;
import net.mydreamy.requirementmodel.rEMODEL.PropertyCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.VariableExpCS;
import org.eclipse.emf.ecore.EObject;

// 字面量、属性、变量的基本类型值处理
public class RValueDealer extends OperationBodyContext{
    private CallExpDealer callExpDealer = new CallExpDealer();
    public UnaryValue getUnaryValue(EObject exp){
        if(exp instanceof PropertyCallExpCS){
            var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) exp);
            if(ret instanceof CallExpDealer.PCGlobalVariable){
                return new BasicVariable(((CallExpDealer.PCGlobalVariable) ret).variable);
            }else if(ret instanceof CallExpDealer.PCAttribute) {
                return ((CallExpDealer.PCAttribute) ret).attributeRef;
            }else{
                throw new UnsupportedOperationException();
            }
        } else if(exp instanceof VariableExpCS){
            return new BasicVariable(variableTable.getVariable(((VariableExpCS) exp).getSymbol()));
        } else if(exp instanceof LiteralExpCS) {
            // todo 字面量
            throw new UnsupportedOperationException();
        } else if(exp instanceof AtomicExpression){
            check(((AtomicExpression) exp).getInfixop() == null);
            return getUnaryValue(((AtomicExpression) exp).getLeftside());
        }else {
            throw new UnsupportedOperationException();
        }
    }
    public BinaryValue getBinaryValue(EObject exp1, EObject exp2, String ops){
        BinaryValue.OP op;
        switch (ops){
            case "+" : op = BinaryValue.OP.ADD; break;
            case "-" : op = BinaryValue.OP.SUB; break;
            case "*" : op = BinaryValue.OP.MULT; break;
            case "/" : op = BinaryValue.OP.DIV; break;
            default:throw new UnsupportedOperationException();
        }
        return new BinaryValue(getUnaryValue(exp1), getUnaryValue(exp2), op);
    }

}

package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.*;
import net.mydreamy.requirementmodel.rEMODEL.*;
import org.eclipse.emf.ecore.EObject;

// 字面量、属性、变量的基本类型值处理
public class RValueDealer extends FactoryContext {
    private final CallExpDealer callExpDealer;

    public RValueDealer(InitPack initPack, CallExpDealer callExpDealer) {
        super(initPack);
        this.callExpDealer = callExpDealer;
    }

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
        } else if(exp instanceof PrimitiveLiteralExpCS) {
            return getLiteralValue((PrimitiveLiteralExpCS) exp);
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
    public LiteralValue getLiteralValue(PrimitiveLiteralExpCS literalExp){
        if(literalExp instanceof BooleanLiteralExpCS){
//            switch (literalExp.getSymbol()){
//                case "true": return new Bool(true);
//                case "false" : return new Bool(false);
//                default: throw new UnsupportedOperationException();
//            }
            return new LiteralValue(literalExp.getSymbol(), new BasicType(BasicType.TypeEnum.BOOLEAN));
        } else if(literalExp instanceof NumberLiteralExpCS){
            if(literalExp.getSymbol().contains(".")){
                return new LiteralValue("float64(" + literalExp.getSymbol() + ")", new BasicType(BasicType.TypeEnum.REAL));
            }else{
                return new LiteralValue("int64(" + literalExp.getSymbol() + ")", new BasicType(BasicType.TypeEnum.INTEGER));
            }
        }else{
            // todo 其他字面量
            throw new UnsupportedOperationException();
        }
    }

}

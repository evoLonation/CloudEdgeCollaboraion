package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.factory.zzy.*;
import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ExpType;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.*;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Bool;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.RValue;
import net.mydreamy.requirementmodel.rEMODEL.*;
import org.eclipse.emf.ecore.EObject;

import java.util.Map;
import java.util.Stack;

/**
 * 接受合约的列表和服务相关的符号表生成出Operation的实际逻辑
 */
public class OperationBodyFactory extends FactoryContext {
    private Contract contract;
    private Location location;
    public OperationBodyFactory(Contract contract, OperationInfo operationInfo, ServiceInfo serviceInfo, Map<String, EntityInfo> entityInfoMap) {
        super(new InitPack(new VariableTable(serviceInfo, operationInfo), entityInfoMap, new Stack<>()));
        this.contract = contract;
        this.location = serviceInfo.getLocation();
        var initPack = getInitPack();
        this.callExpDealer = new CallExpDealer(initPack);
        this.associationDealer = new AssociationDealer(initPack);
        this.rValueDealer = new RValueDealer(initPack, callExpDealer);
        this.logicExpDealer = new LogicExpDealer(initPack, callExpDealer, rValueDealer);
        this.iteratorExpDealer = new IteratorExpDealer(initPack, callExpDealer, associationDealer, logicExpDealer);
    }


    private OperationBody operationBody;
    public void factory(){
        pushLayer();
        buildDefinition();
        buildPrecondition();
        buildPostcondtion();
        operationBody = new OperationBody(popLayer(), location);
    }

    public OperationBody getOperationBody() {
        return operationBody;
    }

    private final IteratorExpDealer iteratorExpDealer ;
    private final CallExpDealer callExpDealer ;
    private final AssociationDealer associationDealer ;
    private final RValueDealer rValueDealer ;
    private final LogicExpDealer logicExpDealer ;

    private void buildDefinition() {
        Definition definition = contract.getDef();
        definition.getVariable().forEach(this::dealDefVariable);
    }

    private void dealDefVariable(VariableDeclarationCS variableDeclaration){
        var variable = new Variable(variableDeclaration.getName(), getType(variableDeclaration.getType()), Variable.ScopeType.DEFINITION);
        var initExp = variableDeclaration.getInitExpression();
        if(initExp instanceof LogicFormulaExpCS){
            initExp = (OCLExpressionCS) ((LogicFormulaExpCS) initExp).getAtomicexp().get(0);
            if(initExp instanceof PropertyCallExpCS) {
                var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) initExp);
                if(ret instanceof CallExpDealer.PCAttribute) {
                    // todo
                }else if(ret instanceof CallExpDealer.PCGlobalVariable) {
                    // todo
                }else if(ret instanceof CallExpDealer.PCAssociation){
                    var ass = ((CallExpDealer.PCAssociation) ret).association;
                    var sourceVar = ((CallExpDealer.PCAssociation) ret).variable;
                    associationDealer.dealAssGet(variable, sourceVar, ass);
                }else{throw new UnsupportedOperationException();}
            }else if(initExp instanceof IteratorExpDealer){
                iteratorExpDealer.dealIteratorExp((IteratorExpCS) initExp, variable);
            }else{throw new UnsupportedOperationException();}
        }else if(initExp instanceof LiteralExpCS){
            // todo
        }
        variableTable.addVariable(variable);
    }

    private void buildPrecondition(){
        var exp = contract.getPre().getOclexp();
        if(exp instanceof BooleanLiteralExpCS){
            var bool = (Bool)rValueDealer.getLiteralValue((BooleanLiteralExpCS)exp);
            check(bool.getValue());
        }else if(exp instanceof LogicFormulaExpCS){
            var logicExp = logicExpDealer.dealLogicFormulaExp((LogicFormulaExpCS) exp, ExpType.PRECONDITION);
            addStatement(new PreCondition(logicExp));
        }else{throw new UnsupportedOperationException();}
    }

    private void buildPostcondtion(){
        dealBlock(contract.getPost().getOclexp());
    }
    private void dealBlock(OCLExpressionCS exp){
        var outExp = exp;
        while (outExp instanceof LetExpCS){
            ((LetExpCS) outExp).getVariable().forEach(v -> {
                check(v.getInitExpression() == null);
                addStatement(new Declare(variableTable.getVariable(v.getName())));
            });
            outExp = ((LetExpCS) outExp).getInExpression();
        }
        check(outExp instanceof LogicFormulaExpCS);
        ((LogicFormulaExpCS)outExp).getAtomicexp().forEach(e ->{
            if(e instanceof AtomicExpression){
                dealSingleStatement((AtomicExpression) e);
            }else if(e instanceof IfExpCS){
                dealIfExp((IfExpCS) e);
            }else{throw new UnsupportedOperationException();}
        });
    }

    private void dealSingleStatement(AtomicExpression atomicExp){
        var left = atomicExp.getLeftside();
        if(atomicExp.getInfixop() == null){
            // 集合的操作
            if(left instanceof StandardNavigationCallExpCS){
                // todo includes, excludes

            }else{throw new UnsupportedOperationException();}
        } else {
            check(atomicExp.getInfixop().equals("="));
            var right = atomicExp.getRightside();
            if(left instanceof PropertyCallExpCS){
                var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) left);
                if(ret instanceof CallExpDealer.PCAttribute){
                    // 属性赋值
                    RValue rValue;
                    if(atomicExp.getOp() == null){
                        rValue = rValueDealer.getUnaryValue(right);
                    }else if(atomicExp.getExp() != null){
                        rValue = rValueDealer.getBinaryValue(right, atomicExp.getExp(), atomicExp.getOp());
                    }else{
                        rValue = rValueDealer.getBinaryValue(right, atomicExp.getNum(), atomicExp.getOp());
                    }
                    addStatement(new Assign(((CallExpDealer.PCAttribute) ret).attributeRef, rValue));
                }else if(ret instanceof CallExpDealer.PCAssociation){
                    var sourceVar = getVariable(right);
                    associationDealer.dealSingleAssAssign(((CallExpDealer.PCAssociation) ret).variable, ((CallExpDealer.PCAssociation) ret).association, sourceVar);
                }else if(ret instanceof CallExpDealer.PCGlobalVariable){
                    var sourceVar = getVariable(right);
                    addStatement(new GlobalAssign(((CallExpDealer.PCGlobalVariable) ret).variable, sourceVar));
                }else{throw new UnsupportedOperationException();}
            }else if(left instanceof VariableExpCS){
                var targetVar = getVariable(left);
                var sourceVar = getVariable(right);
                addStatement(new GlobalAssign(targetVar, sourceVar));
            }else{throw new UnsupportedOperationException();}
        }
    }
    private Variable getVariable(EObject exp){
        if(exp instanceof VariableExpCS){
            return variableTable.getVariable(((VariableExpCS) exp).getSymbol());
        }else if(exp instanceof PropertyCallExpCS){
            return  ((CallExpDealer.PCGlobalVariable) callExpDealer.dealPropertyCall((PropertyCallExpCS) exp)).variable;
        }else{throw new UnsupportedOperationException();}
    }
    private void dealIfExp(IfExpCS ifExp){
        var conditionExp = logicExpDealer.dealLogicFormulaExp((LogicFormulaExpCS) ifExp.getCondition(), ExpType.PRECONDITION);
        pushLayer();
        dealBlock(ifExp.getThenExpression());
        var statementList = popLayer();
        addStatement(new IfBlock(conditionExp, statementList));
    }


}
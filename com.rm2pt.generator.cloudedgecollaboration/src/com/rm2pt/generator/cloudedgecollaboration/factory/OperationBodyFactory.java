package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.VariableTable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Select;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeValue;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.VariableValue;
import net.mydreamy.requirementmodel.rEMODEL.*;
import org.eclipse.emf.ecore.EObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 接受合约的列表和服务相关的符号表生成出Operation的实际逻辑
 */
// todo 写到ServiceInfoFactory里面
public class OperationBodyFactory {
    private ServiceInfo serviceInfo;
    private Operation operation;
    private Contract contract;
    private Map<String, EntityInfo> entityInfoMap;
    public OperationBodyFactory(Contract contract, Operation operation, ServiceInfo serviceInfo, Map<String, EntityInfo> entityInfoMap) {
        this.serviceInfo = serviceInfo;
        this.operation = operation;
        this.contract = contract;
        this.entityInfoMap = entityInfoMap;
    }

    public void factory(){

    }

}
class OperationBodyDeal{
    private VariableTable variableTable;
    private Map<String, EntityInfo> entityInfoMap;


    public OperationBodyDeal(ServiceInfo serviceInfo, Operation operation, Contract contract, Map<String, EntityInfo> entityInfoMap){
        variableTable = new VariableTable(serviceInfo, operation);
        this.entityInfoMap = entityInfoMap;
    }
    private OperationBody getBody(Contract contract) {
        OperationBody operationBody = new OperationBody();
        //todo
        return operationBody;
    }

    private Select getSelect(VariableDeclarationCS variableDeclaration){
        // 定义variable
        String name = variableDeclaration.getName();
        Type type = getType(variableDeclaration.getType());
        Variable variable = new Variable(name, type);
        variableTable.addDefinitionVariable(variable);
        // 得到condition
        LogicFormulaExpCS initExp = (LogicFormulaExpCS) variableDeclaration.getInitExpression();
        Select.Condition condition = getCondition(initExp, (EntityInfo) variable.getType());
        return new Select(variable, condition, new ArrayList<>(), null, null);
    }

    private Type getType(TypeCS typeCS){
        if(typeCS instanceof EntityType){
            EntityType entityType = (EntityType) typeCS;
            return entityInfoMap.get(entityType.getEntity().getName());
        }else{
            throw new UnsupportedOperationException();
        }
    }

    private Select.Condition getCondition(LogicFormulaExpCS initExpression, EntityInfo entityInfo){
        // logic的exps只能有一个
        check(initExpression.getAtomicexp().size() == 1);
        // 子exp只能是atomicExp
        AtomicExpression atomicExpression = (AtomicExpression) initExpression.getAtomicexp().get(0);
        // 只有left
        check(atomicExpression.getRightside() == null);

        IteratorExpCS iteratorExpCS = (IteratorExpCS) atomicExpression.getLeftside();
        ClassiferCallExpCS classiferCallExpCs = (ClassiferCallExpCS) iteratorExpCS.getObjectCall();
        check(classiferCallExpCs.getEntity().equals(entityInfo.getName()));
        check(classiferCallExpCs.getOp().equals("allInstance()"));
        if(iteratorExpCS.getVaribles().size() == 1){

            // 处理初始化变量
            VariableDeclarationCS variableDeclaration = iteratorExpCS.getVaribles().get(0);
            String varName = variableDeclaration.getName();
            EntityType varType = (EntityType) variableDeclaration.getType();
            check(varType.getEntity().getName().equals(entityInfo.getName()));
            // 处理表达式
            // var.attr = ?
            LogicFormulaExpCS allExp = (LogicFormulaExpCS) iteratorExpCS.getExp();
            check(allExp.getAtomicexp().size() == 1);
            AtomicExpression judgeExp = (AtomicExpression) allExp.getAtomicexp().get(0);
            // 左边是 var.attr
            PropertyCallExpCS left = (PropertyCallExpCS) judgeExp.getLeftside();
            check(left.getName().getSymbol().equals(varName));
            check(judgeExp.getInfixop().equals("="));
            String attr = left.getAttribute();
            Value value = getValue(judgeExp.getRightside());
            return new Select.AttributeEquals(attr, value);
        } else {
            throw new UnsupportedOperationException();
        }
    }


    private Value getValue(EObject exp) {
        if(exp instanceof PropertyCallExpCS) {
            PropertyCallExpCS property = (PropertyCallExpCS) exp;
            if(property.getName().getSymbol().equals("self")){
                return new AttributeValue(variableTable.getGlobalVariable(property.getSelfproperty().getSymbol()), property.getAttribute());
            }else{
                return new AttributeValue(variableTable.getLocalVariable(property.getName().getSymbol()), property.getAttribute());
            }
        }else if(exp instanceof VariableExpCS){
            return new VariableValue(variableTable.getLocalVariable(((VariableExpCS) exp).getSymbol()));
        }else if(exp instanceof LogicFormulaExpCS){
            LogicFormulaExpCS lf = (LogicFormulaExpCS) exp;
            if(lf.getAtomicexp().size() == 1){
                return getValue(lf.getAtomicexp().get(0));
            }else{
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void check(boolean condition){
        if(!condition){
            throw new UnsupportedOperationException();
        }
    }

}
package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore.QueryDealer;
import com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore.StoreDealer;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.postcondition.PostconditionCode;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.CollectionOp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Query;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.*;
import net.mydreamy.requirementmodel.rEMODEL.Contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接受合约的列表和服务相关的符号表生成出Operation的实际逻辑
 */
public class OperationBodyFactory{
    private Contract contract;
    private OperationInfo operationInfo;
    private ServiceInfo serviceInfo;
    private Map<String, EntityInfo> entityInfoMap;
    public OperationBodyFactory(Contract contract, OperationInfo operationInfo, ServiceInfo serviceInfo, Map<String, EntityInfo> entityInfoMap) {
        this.contract = contract;
        this.operationInfo = operationInfo;
        this.serviceInfo = serviceInfo;
        // todo 所有的entityName都是首字母小写，待修复
        entityInfoMap.values().forEach(entityInfo -> entityInfo.setName(Keyworder.firstUpperCase(entityInfo.getName())));
        this.entityInfoMap = new HashMap<>();
        entityInfoMap.values().forEach(entityInfo -> this.entityInfoMap.put(entityInfo.getName(), entityInfo));
    }

    private List<Query> queryList;
    private PreCondition preCondition;
    private PostconditionCode postconditionCode;
    private OperationBody operationBody;
    public void factory(){
        StatementFactory statementFactory = new StatementFactory(contract, operationInfo, serviceInfo, entityInfoMap);
        statementFactory.factory();
        List<Statement> statementList = statementFactory.getStatementList();
//        statementList.forEach(this::dealStatement);
        // todo
        operationBody = new OperationBody(statementList);
//        queryList = queryDealer.getQueries();
//        operationBody = new OperationBody(queryList, preCondition, postconditionCode, serviceInfo.getLocation());
    }

    public OperationBody getOperationBody(){
        return operationBody;
    }

    private QueryDealer queryDealer = new QueryDealer();
    private StoreDealer storeDealer = new StoreDealer();

    private void dealStatement(Statement statement){
        if(statement instanceof PreCondition){
            this.preCondition = (PreCondition) statement;
        }else if(statement instanceof CollectionOp){
            queryDealer.addCollectionOp((CollectionOp) statement);
        }else if(statement instanceof Assign){
            var assign = (Assign)statement;
            storeDealer.attributeChanged(assign.getLeft().getVariable(), assign.getLeft().getAttribute());
        }else if(statement instanceof IfBlock){
            // todo
        }else if(statement instanceof ResultAssign) {

        }else{
            throw new UnsupportedOperationException();
        }
    }
}
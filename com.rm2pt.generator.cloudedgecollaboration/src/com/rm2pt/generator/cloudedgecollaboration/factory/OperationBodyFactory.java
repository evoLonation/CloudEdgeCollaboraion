package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.factory.zzy.OperationBodyContext;
import com.rm2pt.generator.cloudedgecollaboration.factory.zzy.VariableTable;
import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import net.mydreamy.requirementmodel.rEMODEL.Contract;

import java.util.Map;

/**
 * 接受合约的列表和服务相关的符号表生成出Operation的实际逻辑
 */
public class OperationBodyFactory extends OperationBodyContext {
    private ServiceInfo serviceInfo;
    private Operation operation;
    private Contract contract;
    public OperationBodyFactory(Contract contract, Operation operation, ServiceInfo serviceInfo, Map<String, EntityInfo> entityInfoMap) {
        this.serviceInfo = serviceInfo;
        this.operation = operation;
        this.contract = contract;
        OperationBodyContext.entityInfoMap = entityInfoMap;
        OperationBodyContext.variableTable = new VariableTable(serviceInfo, operation);
        OperationBodyContext.operationBody = new OperationBody();
    }

    public void factory(){
        operationBody.setLocation(serviceInfo.getLocation());

    }

}
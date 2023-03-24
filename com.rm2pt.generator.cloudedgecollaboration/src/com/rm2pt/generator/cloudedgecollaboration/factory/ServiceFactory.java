package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.*;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import net.mydreamy.requirementmodel.rEMODEL.*;
import net.mydreamy.requirementmodel.rEMODEL.impl.EntityTypeImpl;
import net.mydreamy.requirementmodel.rEMODEL.impl.PrimitiveTypeCSImpl;
import org.eclipse.emf.common.util.EList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需要通过顺序图和服务相关的语法树，以及上一步生成出来的EntitiInfo来得到服务及其操作的签名
 * 遍历Service的Operation的时候，需要同时扫描顺序图，检测每个Operation实际上是Operation还是Logic
 * 1、生成出所有的Service
 * 2、Operation只需要签名，不用生成Body部分
 * 3、Logic需要生成出签名和流程
 */
public class ServiceFactory {
    private final List<Interaction> interactionList;
    private final List<Service> serviceList;
    private final List<Contract> contractList;
    private final Map<String, EntityInfo> entityMap;
    private final List<ServiceInfo> serviceInfoList;
    private final Map<String, ServiceInfo> serviceInfoMap;

    public ServiceFactory(List<Interaction> interactionList, List<Service> serviceList, List<Contract> contractList, Map<String, EntityInfo> entityMap) {
        System.out.println("\nServiceFactory:");

        this.interactionList = interactionList;
        System.out.println("interactionList size: " + interactionList.size());
        for (Interaction interaction : interactionList) {
            System.out.println("interaction: " + interaction.getName());
        }

        this.serviceList = serviceList;
        System.out.println("serviceList size: " + serviceList.size());
        for (Service service : serviceList) {
            System.out.println("service: " + service.getName());
        }

        this.contractList = contractList;
        System.out.println("contractList size: " + contractList.size());
        for (Contract contract : contractList) {
            System.out.println("contract: " + contract.getOp().getName());
        }

        this.entityMap = entityMap;
        System.out.println("entityMap size: " + entityMap.size());
        for (Map.Entry<String, EntityInfo> entry : entityMap.entrySet()) {
            String mapKey = entry.getKey();
            EntityInfo mapValue = entry.getValue();
            System.out.println(mapKey + "：" + mapValue);
        }
        System.out.println("lalala" + entityMap.get("item"));

        serviceInfoList = new ArrayList<ServiceInfo>();
        serviceInfoMap = new HashMap<String, ServiceInfo>();
    }

    public void factory() {
        if (interactionList.size() == 0) {
            throw new RuntimeException("interactionList empty");
        }
        if (serviceList.size() == 0) {
            throw new RuntimeException("serviceList empty");
        }
        if (contractList.size() == 0) {
            throw new RuntimeException("contractList empty");
        }

        for (Service service : serviceList) {
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setName(service.getName());
            System.out.println("\nservice name: " + service.getName());

            EList<Operation> operationEList = service.getOperation();
            List<OperationInfo> operationInfos = new ArrayList<>();
            Map<String, OperationInfo> operationMap = new HashMap<>();
            for (Operation o : operationEList) {
                OperationInfo operationInfo = new OperationInfo();
                operationInfo.setName(o.getName());
                System.out.println("operation name: " + o.getName());

                for (Contract c : contractList) {
                    if (c.getOp().getName().equals(o.getName())) {
                        System.out.println("\tGet parameters:");
                        EList<Parameter> parameters = c.getOp().getParameter();
                        List<Variable> inputParamList = new ArrayList<Variable>();
                        for (Parameter p : parameters) {
                            Variable variable = new Variable(p.getName(), convertTypeCSToType(p.getType()), Variable.ScopeType.PARAM);
                            inputParamList.add(variable);
                            System.out.println("\t\tparameter name: " + p.getName());
                        }
                        operationInfo.setInputParamList(inputParamList);

                        System.out.println("\tGet return type:");
                        if (c.getOp().getReturnType() != null) {
                            operationInfo.setReturnType(convertTypeCSToType(c.getOp().getReturnType()));
                            System.out.println("\t\treturn type: " + c.getOp().getReturnType());
                        }
                        // todo operationbody
                        OperationBodyFactory operationBodyFactory = new OperationBodyFactory(c, operationInfo, serviceInfo, entityMap);
                        operationBodyFactory.factory();
                        OperationBody operationBody = operationBodyFactory.getOperationBody();
                        operationInfo.setOperationBody(operationBody);
                        break;
                    }
                }



                if (operationInfo.getName().equals("enterItems")) {
                    operationInfo.setConcurrencyType(OperationInfo.ConcurrencyType.HIGHPRIORITY);
                } else if (operationInfo.getName().equals("checkItem")) {
                    operationInfo.setConcurrencyType(OperationInfo.ConcurrencyType.WEAKCONSISTENCY);
                } else {
                    operationInfo.setConcurrencyType(OperationInfo.ConcurrencyType.DEFAULT);
                }

                operationInfos.add(operationInfo);
                operationMap.put(operationInfo.getName(), operationInfo);
            }
            serviceInfo.setOperationList(operationInfos);

            serviceInfoList.add(serviceInfo);
            serviceInfoMap.put(service.getName(), serviceInfo);
        }
    }

    public Type convertTypeCSToType(TypeCS typeCS) {
        if (typeCS instanceof PrimitiveTypeCSImpl) {
            BasicType type;
            switch (((PrimitiveTypeCSImpl) typeCS).getName()) {
                case "Real":
                    type = new BasicType(BasicType.TypeEnum.REAL);
                    break;
                case "Integer":
                    type = new BasicType(BasicType.TypeEnum.INTEGER);
                    break;
                case "Date":
                    type = new BasicType(BasicType.TypeEnum.TIME);
                    break;
                case "Boolean":
                    type = new BasicType(BasicType.TypeEnum.BOOLEAN);
                    break;
                case "String":
                    type = new BasicType(BasicType.TypeEnum.STRING);
                    break;
                default:
                    throw new RuntimeException("Invalid PrimitiveTypeCS: " + ((PrimitiveTypeCSImpl) typeCS).getName());
            }
            return type;
        } else if (typeCS instanceof EntityTypeImpl) {
            System.out.println("entity name: " + ((EntityTypeImpl) typeCS).getEntity().getName().toLowerCase());
            EntityInfo entityInfo = entityMap.get(((EntityTypeImpl) typeCS).getEntity().getName().toLowerCase());
            if (entityInfo == null) {
                throw new RuntimeException("Entity not found: " + ((EntityTypeImpl) typeCS).getEntity().getName().toLowerCase());
            }
            return new EntityTypeInfo(entityInfo);
        } else {
            throw new RuntimeException("Invalid TypeCS: " + typeCS);
        }
    }

    public List<ServiceInfo> getServiceList() {
        return serviceInfoList;
    }

    /**
     * @return key是Service的名字
     */
    public Map<String, ServiceInfo> getServiceMap() {
        return serviceInfoMap;
    }

    // todo new
    public List<OperationInfo> getOperation(OperationInfo.ConcurrencyType concurrencyType) {
        List<OperationInfo> list = new ArrayList<>();
        for (ServiceInfo serviceInfo : serviceInfoList) {
            for (OperationInfo operationInfo : serviceInfo.getOperationList()) {
                if (operationInfo.getConcurrencyType() == concurrencyType) {
                    list.add(operationInfo);
                }
            }
        }
        return list;
    }
}

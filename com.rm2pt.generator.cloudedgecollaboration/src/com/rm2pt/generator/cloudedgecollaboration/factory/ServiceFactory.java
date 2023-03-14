package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.Logic;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import net.mydreamy.requirementmodel.rEMODEL.*;
import net.mydreamy.requirementmodel.rEMODEL.impl.*;
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
        this.interactionList = interactionList;
        this.serviceList = serviceList;
        this.contractList = contractList;
        this.entityMap = entityMap;
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
            if (service.getName().startsWith("Cloud")) {
                serviceInfo.setName(service.getName().replace("Cloud", ""));
                serviceInfo.setLocationType(ServiceInfo.LocationType.CLOUD);
            } else if (service.getName().startsWith("Edge")) {
                serviceInfo.setName(service.getName().replace("Edge", ""));
                serviceInfo.setLocationType(ServiceInfo.LocationType.EDGE);
            } else {
                throw new RuntimeException("Invalid Service Name: " + service.getName());
            }

            EList<Attribute> tempProperties = service.getTemp_property();
            List<Variable> variables = new ArrayList<Variable>();
            for (Attribute attribute : tempProperties) {
                Variable variable = new Variable();
                variable.setName(attribute.getName());
                variable.setType(convertTypeCSToType(attribute.getType()));
                variables.add(variable);
            }
            serviceInfo.setGlobalVariableList(variables);

            EList<Operation> operationEList = service.getOperation();
            List<com.rm2pt.generator.cloudedgecollaboration.info.Operation> operations
                    = new ArrayList<>();
            Map<String, com.rm2pt.generator.cloudedgecollaboration.info.Operation> operationMap
                    = new HashMap<>();
            for (Operation o : operationEList) {
                com.rm2pt.generator.cloudedgecollaboration.info.Operation operation = new com.rm2pt.generator.cloudedgecollaboration.info.Operation();
                operation.setName(o.getName());

                EList<Parameter> parameters = o.getParameter();
                List<Variable> inputParamList = new ArrayList<Variable>();
                for (Parameter p : parameters) {
                    Variable variable = new Variable();
                    variable.setName(p.getName());
                    variable.setType(convertTypeCSToType(p.getType()));
                    inputParamList.add(variable);
                }
                operation.setInputParamList(inputParamList);

                for (Contract c : contractList) {
                    if (c.getOp().getName().equals(o.getName())) {
                        if (c.getOp().getReturnType() != null) {
                            operation.setReturnType(convertTypeCSToType(c.getOp().getReturnType()));
                        }
                        break;
                    }
                }

                // TODO: OperationBody

                operations.add(operation);
                operationMap.put(operation.getName(), operation);
            }
            serviceInfo.setOperationList(operations);

            boolean logicFound = false;
            for (Interaction i : interactionList) {
                EList<Message> messages = i.getMessages();
                for (Message m : messages) {
                    if (m instanceof CallMessageImpl) {
                        Operation op = ((CallMessageImpl) m).getOp();
                        com.rm2pt.generator.cloudedgecollaboration.info.Operation operation = operationMap.get(op.getName());
                        if (operation != null) {
                            MixEnd sender = m.getSendingEnd();
                            MixEnd receiver = m.getReceivingEnd();
                            if (sender.getContext() instanceof ActorImpl && receiver.getContext() instanceof ServiceImpl) {
                                Logic logic = new Logic();
                                logic.setCaller(Logic.Caller.USER);
                                logic.setName(operation.getName());
                                logic.setInputParamList(operation.getInputParamList());
                                logic.setReturnType(operation.getReturnType());

                                // TODO: LogicBody

                                logic.setServiceToCall(serviceInfo);
                                serviceInfo.setLogic(logic);

                                if (logic.getReturnType() != null) {
                                    serviceInfo.setInteractiveType(ServiceInfo.InteractiveType.LOGICTOUSER);
                                } else {
                                    serviceInfo.setInteractiveType(ServiceInfo.InteractiveType.LOGICNORETURN);
                                }

                                logicFound = true;
                            } else if (sender.getContext() instanceof ServiceImpl && receiver.getContext() instanceof ActorImpl) {
                                Logic logic = new Logic();
                                logic.setCaller(Logic.Caller.NODE);
                                logic.setName(operation.getName());
                                logic.setInputParamList(operation.getInputParamList());
                                logic.setReturnType(operation.getReturnType());

                                // TODO: LogicBody

                                logic.setServiceToCall(serviceInfo);
                                serviceInfo.setLogic(logic);

                                if (logic.getReturnType() != null) {
                                    serviceInfo.setInteractiveType(ServiceInfo.InteractiveType.LOGICTONODE);
                                } else {
                                    serviceInfo.setInteractiveType(ServiceInfo.InteractiveType.LOGICNORETURN);
                                }

                                logicFound = true;
                            }
                        }
                    }

                    if (logicFound) {
                        break;
                    }
                }

                if (logicFound) {
                    break;
                }
            }

            if (serviceInfo.getLogic() == null) {
                serviceInfo.setInteractiveType(ServiceInfo.InteractiveType.NORMAL);
            }

            if (serviceInfo.getName().startsWith("Edge")) {
                serviceInfo.setLocationType(ServiceInfo.LocationType.EDGE);
            } else if (serviceInfo.getName().startsWith("Cloud")) {
                serviceInfo.setLocationType(ServiceInfo.LocationType.CLOUD);
            }

            serviceInfoList.add(serviceInfo);
            serviceInfoMap.put(service.getName(), serviceInfo);
        }
    }

    public Type convertTypeCSToType(TypeCS typeCS) {
        if (typeCS instanceof PrimitiveTypeCSImpl) {
            BasicType type = new BasicType() {
            };
            switch (((PrimitiveTypeCSImpl) typeCS).getName()) {
                case "Real":
                    type.setTypeEnum(BasicType.TypeEnum.REAL);
                    break;
                case "Integer":
                    type.setTypeEnum(BasicType.TypeEnum.INTEGER);
                    break;
                case "Date":
                    type.setTypeEnum(BasicType.TypeEnum.TIME);
                    break;
                case "Boolean":
                    type.setTypeEnum(BasicType.TypeEnum.BOOLEAN);
                    break;
                case "String":
                    type.setTypeEnum(BasicType.TypeEnum.STRING);
                    break;
                default:
                    throw new RuntimeException("Invalid PrimitiveTypeCS: " + ((PrimitiveTypeCSImpl) typeCS).getName());
            }
            return type;
        } else if (typeCS instanceof EntityTypeImpl) {
            EntityInfo entityInfo = entityMap.get(((EntityTypeImpl) typeCS).getEntity().getName());
            if (entityInfo == null) {
                throw new RuntimeException("Entity not found: " + ((EntityTypeImpl) typeCS).getEntity().getName());
            }
            return entityInfo;
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


    /**
     * @return 返回满足参数条件的服务，要求复杂度O(1)
     */
    public List<ServiceInfo> getServiceList(ServiceInfo.LocationType locationType, ServiceInfo.InteractiveType interactiveType) {
        List<ServiceInfo> list = new ArrayList<ServiceInfo>();
        for (ServiceInfo serviceInfo : serviceInfoList) {
            if (serviceInfo.getLocationType() == locationType && serviceInfo.getInteractiveType() == interactiveType) {
                list.add(serviceInfo);
            }
        }
        return list;
    }
}

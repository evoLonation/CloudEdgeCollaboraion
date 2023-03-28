package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.factory.OperationBodyFactory;
import com.rm2pt.generator.cloudedgecollaboration.generator.OperationBodyGenerator;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceParser {
    public static String parseOperationParameter(OperationInfo operation) {
        String parameterStr;
        List<Variable> inputParamList = operation.getInputParamList();
        StringBuilder sb = new StringBuilder();
        for (Variable variable : inputParamList) {
            sb.append(variable.getName()).append(" ").append(parseType(variable.getType())).append(", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        parameterStr = sb.toString();
        return parameterStr;
    }

    public static String parseOperationParameterWithoutType(GolangOperation operation) {
        String parameterStr;
        List<Variable> inputParamList = operation.getInputParamList();
        StringBuilder sb = new StringBuilder();
        for (Variable variable : inputParamList) {
            sb.append(variable.getName()).append(", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        parameterStr = sb.toString();
        return parameterStr;
    }

    public static List<GolangOperation> parseOperationErr(ServiceInfo service, List<GolangOperation> operationList, GolangOperation operation) {
        List<OperationInfo> ops = service.getOperationList();
        List<GolangOperation> goOps = new ArrayList<>();
        for (int i = 0; i < ops.size(); i++) {
            OperationInfo op = ops.get(i);
            goOps.add(operationList.get(i));
            if (op.getName().equals(operation.getName())) {
                break;
            }
        }
        return goOps;
    }

    public static String parseOperationReturn(OperationInfo operation) {
        String returnStr;
        Type returnType = operation.getReturnType();
        if (returnType == null) {
            returnStr = "";
        } else {
            returnStr = "result " + parseType(returnType);
        }
        return returnStr;
    }

    public static String parseContextServiceParameter(List<ServiceInfo> serviceInfoList) {
        StringBuilder sb = new StringBuilder();
        for (ServiceInfo serviceInfo : serviceInfoList) {
            sb.append(parseServiceName(serviceInfo, false)).append(" *").append(parseServiceName(serviceInfo, true)).append(", ");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public static String parseServiceName(ServiceInfo service, boolean isEntity) {
        String serviceName = service.getName();
        if (isEntity) {
            serviceName = serviceName.substring(0, 1).toUpperCase() + serviceName.substring(1);
        } else {
            serviceName = serviceName.substring(0, 1).toLowerCase() + serviceName.substring(1);
        }
        return serviceName;
    }

    public static String parseOperationName(OperationInfo operation, boolean isEntity) {
        String operationName = operation.getName();
        if (isEntity) {
            operationName = operationName.substring(0, 1).toUpperCase() + operationName.substring(1);
        } else {
            operationName = operationName.substring(0, 1).toLowerCase() + operationName.substring(1);
        }
        return operationName;
    }

    public static String getHPOBodyString(OperationInfo operation) {
        OperationBodyGenerator operationBodyGenerator = new OperationBodyGenerator(operation.getOperationBody(), operation.getConcurrencyType() != OperationInfo.ConcurrencyType.WEAKCONSISTENCY);
        return operationBodyGenerator.generateHighPriority();
    }

    public static String getOperationBodyString(OperationInfo operation) {
        OperationBodyGenerator operationBodyGenerator = new OperationBodyGenerator(operation.getOperationBody(), operation.getConcurrencyType() != OperationInfo.ConcurrencyType.WEAKCONSISTENCY);
        return operationBodyGenerator.generate();
    }

    public static String parseType(Type type) {
        if (type instanceof BasicType) {
            switch (((BasicType) type).getTypeEnum().name()) {
                case "INTEGER":
                    return "int64";
                case "TIME":
                    return "time.Time";
                case "REAL":
                    return "float64";
                case "STRING":
                    return "string";
                case "BOOLEAN":
                    return "bool";
                default:
                    throw new RuntimeException("Unknown entityType: " + type);
            }
        } else if (type instanceof EntityTypeInfo) {
            EntityInfo entityInfo = ((EntityTypeInfo) type).getEntityInfo();
            return "entity." + entityInfo.getName().substring(0, 1).toUpperCase() + entityInfo.getName().substring(1);
        } else {
            throw new RuntimeException("Unknown entityType: " + type);
        }
    }
}

package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceParser {
    public static String parseOperationParameter(GolangOperation operation) {
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

    public static String parseType(Type type) {
        if (type instanceof BasicType) {
            switch (((BasicType) type).getTypeEnum().name()) {
                case "INTEGER":
                    return "int32";
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
            return "entity." + entityInfo.getName();
        } else {
            throw new RuntimeException("Unknown entityType: " + type);
        }
    }
}

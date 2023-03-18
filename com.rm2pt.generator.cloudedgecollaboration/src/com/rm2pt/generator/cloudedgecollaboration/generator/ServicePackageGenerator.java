package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.*;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.ArrayList;
import java.util.List;

public class ServicePackageGenerator extends Generator {
    private final List<ServiceInfo> serviceList;

    public ServicePackageGenerator(List<ServiceInfo> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    protected void generate() {
        for (ServiceInfo service : serviceList) {
            String golangCode = "";

            List<Variable> globalVariableList = service.getGlobalVariableList();
            List<Operation> operationList = service.getOperationList();
            List<GolangOperation> golangOperationList = new ArrayList<>();
            for (int i = 0; i < operationList.size(); i++) {
                if (i != operationList.size() - 1) {
                    GolangOperation golangOperation = new GolangOperation(operationList.get(i), GolangOperation.OperationScope.PRIVATE);
                    golangOperationList.add(golangOperation);
                } else {
                    GolangOperation golangOperation = new GolangOperation(operationList.get(i), GolangOperation.OperationScope.PUBLIC);
                    golangOperationList.add(golangOperation);
                }
            }

            String serviceFileName = service.getName() + ".go";
            if (service.getLocation() == Location.CLOUD) {
                serviceFileName = "cloud/service/" + serviceFileName;

                golangCode = CloudServiceTemplate.generateService(service, globalVariableList, golangOperationList);
            } else if (service.getLocation() == Location.EDGE) {
                serviceFileName = "edge/service/" + serviceFileName;
                golangCode = EdgeServiceTemplate.generateService(service, globalVariableList, golangOperationList);
            } else {
                throw new RuntimeException("Service location error");
            }

            generateFile(serviceFileName, golangCode);
            System.out.println(serviceFileName + " generated");
        }
    }
}

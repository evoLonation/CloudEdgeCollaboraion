package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.info.Operation;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.*;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class ServicePackageGenerator extends Generator {
    private final List<ServiceInfo> serviceList;

    public ServicePackageGenerator(List<ServiceInfo> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    protected void generate() {
        for (ServiceInfo service : serviceList) {
            List<Variable> globalVariableList = service.getGlobalVariableList();
            List<Operation> operationList = service.getOperationList();

            String serviceFileName = service.getName() + ".go";
            String golangCode = ServiceTemplate.generateService(service, globalVariableList, operationList);
            generateFile(serviceFileName, golangCode);
            System.out.println(serviceFileName + " generated");
        }
    }
}

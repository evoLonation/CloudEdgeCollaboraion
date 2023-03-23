package com.rm2pt.generator.cloudedgecollaboration.generator;

import java.util.ArrayList;
import java.util.List;

import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.ContextTemplate;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.HighPriorityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.ServiceTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;

// todo
public class ServicePackageGenerator extends Generator {
    private final List<ServiceInfo> serviceList;

    public ServicePackageGenerator(List<ServiceInfo> serviceList, List<OperationInfo> highPriOperationList) {
        this.serviceList = serviceList;
    }

    @Override
    public void generate() {
        System.out.println("Generating service package: ");

        String golangCode = "";
        List<OperationInfo> highPriOperationList = new ArrayList<>();
        for (ServiceInfo service : serviceList) {
            List<OperationInfo> operationInfoList = service.getOperationList();
            for (OperationInfo operationInfo : operationInfoList) {
                if (operationInfo.getConcurrencyType() == OperationInfo.ConcurrencyType.HIGHPRIORITY) {
                    highPriOperationList.add(operationInfo);
                }
            }
        }

        golangCode = ContextTemplate.generateContext(serviceList, highPriOperationList);
        generateFile("service/context.go", golangCode);
        System.out.println("service/context.go generated");

        golangCode = HighPriorityTemplate.generateHighPriority(highPriOperationList);
        generateFile("service/highpriority.go", golangCode);
        System.out.println("service/highpriority.go generated");

        for (ServiceInfo service : serviceList) {
            System.out.println("\tGenerating service: " + service.getName());
            String serviceFileName = "service/" +
                    service.getName().substring(0, 1).toLowerCase() + service.getName().substring(1) +
                    ".go";
            golangCode = ServiceTemplate.generateService(service);
            generateFile(serviceFileName, golangCode);
            System.out.println(serviceFileName + " generated");
        }
    }
}

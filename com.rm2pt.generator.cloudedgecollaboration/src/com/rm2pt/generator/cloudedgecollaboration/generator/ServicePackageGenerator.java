package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.*;

import java.util.List;

public class ServicePackageGenerator extends Generator {
    private final List<ServiceInfo> serviceList;

    public ServicePackageGenerator(List<ServiceInfo> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    protected void generate() {
        for (ServiceInfo service : serviceList) {
            String serviceFileName = service.getName() + ".go";
            String golangCode = ServiceTemplate.generateService(ServiceInfo);
            generateFile(serviceFileName, golangCode);
            System.out.println(serviceFileName + " generated");
        }
    }
}

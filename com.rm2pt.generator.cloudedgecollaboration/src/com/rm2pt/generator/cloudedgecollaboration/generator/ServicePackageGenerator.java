package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

public class ServicePackageGenerator extends Generator {
    private List<ServiceInfo> serviceList;
    private List<String> servicePackage;

    public ServicePackageGenerator(List<ServiceInfo> serviceList) {
        this.serviceList = serviceList;
        this.servicePackage = new ArrayList<>();
    }

    @Override
    protected void generate() {
        for (ServiceInfo service : serviceList) {
            StringBuilder sb = new StringBuilder();

            servicePackage.add(sb.toString());
        }
    }
}

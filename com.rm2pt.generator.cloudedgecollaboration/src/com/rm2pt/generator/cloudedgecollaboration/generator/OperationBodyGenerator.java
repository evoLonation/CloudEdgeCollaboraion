package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.ServiceTemplate;

public class OperationBodyGenerator {
    private OperationBody operationBody;
    public OperationBodyGenerator(OperationBody operationBody){

    }
    public String generate() {
        return ServiceTemplate.context();
    }

    public String generateHighPriority(){
        return ServiceTemplate.context();
    }
}

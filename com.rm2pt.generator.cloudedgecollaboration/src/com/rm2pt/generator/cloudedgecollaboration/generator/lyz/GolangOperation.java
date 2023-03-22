package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;

public class GolangOperation extends OperationInfo {
    public enum OperationScope {
        PUBLIC,
        PRIVATE
    }

    private OperationScope operationScope;

    public GolangOperation(OperationInfo operationInfo, OperationScope operationScope) {
        this.operationScope = OperationScope.PUBLIC;
        if (operationScope == OperationScope.PRIVATE) {
            this.setName(Keyworder.firstLowerCase(operationInfo.getName()));
        } else {
            this.setName(Keyworder.firstUpperCase(operationInfo.getName()));
        }
        this.setInputParamList(operationInfo.getInputParamList());
        this.setReturnType(operationInfo.getReturnType());
        this.setOperationBody(operationInfo.getOperationBody());
    }

    public OperationScope getOperationScope() {
        return operationScope;
    }

    public void setOperationScope(OperationScope operationScope) {
        this.operationScope = operationScope;
    }

    public boolean isPublic() {
        return operationScope == OperationScope.PUBLIC;
    }
}

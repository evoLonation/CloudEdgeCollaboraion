package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.Operation;

public class GolangOperation extends Operation {
    public enum OperationScope {
        PUBLIC,
        PRIVATE
    }

    private OperationScope operationScope;

    public GolangOperation(Operation operation, OperationScope operationScope) {
        this.operationScope = OperationScope.PUBLIC;
        if (operationScope == OperationScope.PRIVATE) {
            this.setName(Keyworder.firstLowerCase(operation.getName()));
        } else {
            this.setName(Keyworder.firstUpperCase(operation.getName()));
        }
        this.setInputParamList(operation.getInputParamList());
        this.setReturnType(operation.getReturnType());
        this.setOperationBody(operation.getOperationBody());
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

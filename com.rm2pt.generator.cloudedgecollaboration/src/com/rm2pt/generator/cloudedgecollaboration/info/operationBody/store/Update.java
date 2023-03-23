package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.Set;

public class Update extends Store {
    public Update(Variable variable, Set<EntityInfo.Attribute> attributeSet) {
        super(variable, attributeSet);
    }
}

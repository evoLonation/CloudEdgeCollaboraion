package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.Set;

public class Insert extends Store {
    public Insert(Variable variable, Set<EntityInfo.Attribute> attributeSet) {
        super(variable, attributeSet);
    }
}

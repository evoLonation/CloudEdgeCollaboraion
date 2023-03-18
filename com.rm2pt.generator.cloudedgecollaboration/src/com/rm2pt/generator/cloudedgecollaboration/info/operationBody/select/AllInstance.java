package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;

public class AllInstance extends Source {
    private EntityInfo entityInfo;

    @Override
    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public AllInstance(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.data;

public class EntityTypeInfo extends Type{
    private EntityInfo entityInfo;

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public EntityTypeInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    @Override
    public boolean equals(Object obj) {
        return ((EntityTypeInfo)obj).entityInfo.equals(entityInfo);
    }
}

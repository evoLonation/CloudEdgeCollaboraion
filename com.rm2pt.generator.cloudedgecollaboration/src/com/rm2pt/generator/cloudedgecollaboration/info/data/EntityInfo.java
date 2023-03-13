package com.rm2pt.generator.cloudedgecollaboration.info.data;

import java.util.List;

public class EntityInfo extends Type {
    private String name;
    private Variable id;
    private List<Variable> attributeList;
    private StorageType storageType;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Variable id) {
        this.id = id;
    }

    public void setAttributeList(List<Variable> attributeList) {
        this.attributeList = attributeList;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public enum StorageType{
        CLOUD,
        EDGE,
        CACHE;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getAttributeList() {
        return attributeList;
    }

    public StorageType getStorageType() {
        return storageType;
    }
}

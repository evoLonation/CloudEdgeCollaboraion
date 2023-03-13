package com.rm2pt.generator.cloudedgecollaboration.info.data;

import java.util.List;

public class EntityInfo extends Type {
    private String name;
    private List<Variable> attributeList;
    private StorageType storageType;
    public enum StorageType{
        CLOUD,
        EDGE,
        CACHE;
    }
}

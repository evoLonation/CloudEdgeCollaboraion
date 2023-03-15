package com.rm2pt.generator.cloudedgecollaboration.info.data;

import java.util.Map;

public class EntityInfo extends Type {
    private String name;
    private StorageType storageType;

    public void setName(String name) {
        this.name = name;
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

    public StorageType getStorageType() {
        return storageType;
    }



    private Attribute idAttribute;
    //需要包含属性、id和关联转化的属性
    private Map<String, Variable> attributeMap;
    //所有的关联
    private Map<String, AssociationInfo> associationMap;
    // 原来的entity中的各种key对应的类型
    private Map<String, KeyType> keyTypeMap;

    public enum KeyType {
        ATTRIBUTE,
        ASSOCIATION;
    }

    public static class Attribute{
        private String name;
        private BasicType type;
    }

    public static abstract class AssociationInfo {
        private String name;
        private EntityInfo targetEntity;
        private boolean isMulti;
    }


    public static class ForeignKeyAss extends AssociationInfo{
        //指向关联对应的外键
        private String attributeName;
    }
    public static class JoinTableAss extends AssociationInfo {
        // todo 暂时不用
    }

    public KeyType getKeyType(String name){
        throw new UnsupportedOperationException();
    }

    public Attribute getIdAttribute() {
        return idAttribute;
    }

    public Attribute getAttribute(String name){
        throw new UnsupportedOperationException();
    }
    public AssociationInfo getAssociation(String name){
        throw new UnsupportedOperationException();
    }
}

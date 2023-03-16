package com.rm2pt.generator.cloudedgecollaboration.info.data;

import java.util.List;
import java.util.Map;

public class EntityInfo {
    private String name;
    private StorageType storageType;
    private Attribute idAttribute;
    private Map<String, Attribute> attributeMap;
    private Map<String, Association> associationMap;
    private Map<String, KeyType> keyTypeMap;
    private List<Attribute> attributeList;
    private List<Association> associationList;

    public enum StorageType {
        CLOUD,
        EDGE,
        CACHE
    }

    public String getName() {
        return name;
    }

    public void setIdAttribute(Attribute idAttribute) {
        this.idAttribute = idAttribute;
    }

    public Attribute getIdAttribute() {
        return idAttribute;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public List<Association> getAssociationList() {
        return associationList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public void setAssociationList(List<Association> associationList) {
        this.associationList = associationList;
    }

    public void addAttribute(Attribute attribute){
        attributeList.add(attribute);
    }

    public void addAssociation(Association association){
        associationList.add(association);
    }

    public void setAttributeMap(Map<String, Attribute> attributeMap) {
        this.attributeMap = attributeMap;
    }

    public void setAssociationMap(Map<String, Association> associationMap) {
        this.associationMap = associationMap;
    }

    public void setKeyTypeMap(Map<String, KeyType> keyTypeMap) {
        this.keyTypeMap = keyTypeMap;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public enum KeyType {
        ATTRIBUTE,
        ASSOCIATION
    }

    public static class Attribute {
        private String name;
        private BasicType type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BasicType getType() {
            return type;
        }

        public void setType(BasicType type) {
            this.type = type;
        }
    }

    public static abstract class Association {
        private String name;
        private EntityInfo targetEntity;
        private boolean isMulti;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public EntityInfo getTargetEntity() {
            return targetEntity;
        }

        public void setTargetEntity(EntityInfo targetEntity) {
            this.targetEntity = targetEntity;
        }

        public boolean isMulti() {
            return isMulti;
        }

        public void setMulti(boolean multi) {
            isMulti = multi;
        }
    }

    public static class ForeignKeyAss extends Association {
        //指向关联对应的外键
        private String refAttrName;
        private BasicType type;

        public String getRefAttrName() {
            return refAttrName;
        }

        public void setRefAttrName(String name) {
            this.refAttrName = name;
        }

        public BasicType getType() {
            return type;
        }

        public void setType(BasicType type) {
            this.type = type;
        }
    }

    public static class JoinTableAss extends Association {
        // todo 暂时不用
    }


    public KeyType getKeyType(String name) {
        return keyTypeMap.get(name);
    }

    public Attribute getAttribute(String name) {
        return attributeMap.get(name);
    }

    public Association getAssociation(String name) {
        return associationMap.get(name);
    }
}

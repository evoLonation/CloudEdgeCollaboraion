package com.rm2pt.generator.cloudedgecollaboration.generator;


import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.AttributeStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;

import java.util.ArrayList;
import java.util.List;

public class DDLGenerator extends Generator {
    private final List<EntityInfo> infoList;
    private final List<EntityStr> SQLCloudEntityList, SQLEdgeEntityList;
    private String SQLText;

    public DDLGenerator(List<EntityInfo> EntityInfoList) {
        infoList = new ArrayList<>();
        infoList.addAll(EntityInfoList);
        SQLText = "";
        SQLCloudEntityList = new ArrayList<>();
        SQLEdgeEntityList = new ArrayList<>();
    }

    @Override
    public void generate() {
        for (EntityInfo entityInfo : infoList) {
            String entityName = entityInfo.getName();
            String storageType = entityInfo.getStorageType().name();

            String idName = entityInfo.getIdAttribute().getName();
            String idType = entityInfo.getIdAttribute().getType().getTypeEnum().name();
            AttributeStr primaryKey = new AttributeStr(
                    Keyworder.camelToUnderScore(idName),
                    basicType2SQLType(idType));

            List<AttributeStr> attributes = new ArrayList<>();
            for (EntityInfo.Attribute attr : entityInfo.getAttributeList()) {
                if (attr.getName().equals(idName)){
                    continue;
                }
                String attrName = attr.getName();
                String attrType = attr.getType().getTypeEnum().name();
                attributes.add(new AttributeStr(
                        Keyworder.camelToUnderScore(attrName),
                        basicType2SQLType(attrType)));
            }

            for (EntityInfo.Association asso : entityInfo.getAssociationList()) {
                if (!(asso instanceof EntityInfo.ForeignKeyAss)) {
                    throw new AssertionError("association is not a foreign key!");
                }
                if (asso.isMulti()) {
                    continue;
                }
                String assoName = ((EntityInfo.ForeignKeyAss) asso).getRefAttrName();
                String assoType = ((EntityInfo.ForeignKeyAss) asso).getType().getTypeEnum().name();
                attributes.add(new AttributeStr(
                        Keyworder.camelToUnderScore(assoName),
                        basicType2SQLType(assoType)));
            }

            EntityStr entityStr = new EntityStr(entityName, primaryKey, attributes);
            if (storageType.equals(EntityInfo.StorageType.EDGE.name())){
                SQLEdgeEntityList.add(entityStr);
            }else {
                SQLCloudEntityList.add(entityStr);
            }
        }
        SQLText = EntityTemplate.SQLContext(SQLCloudEntityList);
        generateFile("build/cloud.sql", SQLText);
        SQLText = EntityTemplate.SQLContext(SQLEdgeEntityList);
        generateFile("build/edge.sql", SQLText);
        System.out.println("Generated_DDL");
    }

    private String basicType2SQLType(String type) {
        switch (type) {
            case "INTEGER":
                return "INT";
            case "TIME":
                return "DATETIME";
            case "REAL":
                return "DOUBLE";
            case "STRING":
                return "VARCHAR(255)";
            case "BOOLEAN":
                return "BOOLEAN";
            default:
                throw new RuntimeException("Unknown entityType: " + type);
        }
    }


}



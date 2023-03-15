package com.rm2pt.generator.cloudedgecollaboration.generator;


import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.AttributeStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

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

            String idName = entityInfo.getId().getName();
            String idType = entityInfo.getId().getType().getName();
            AttributeStr primaryKey = new AttributeStr(
                    Keyworder.camelToUnderScore(idName),
                    entityType2SQLType(idType));

            List<AttributeStr> attributes = new ArrayList<>();
            for (Variable attr : entityInfo.getAttributeList()){
                String attrName = attr.getName();
                String attrType = attr.getType().getName();
                attributes.add(new AttributeStr(
                        Keyworder.camelToUnderScore(attrName),
                        entityType2SQLType(attrType)));
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

    private String entityType2SQLType(String type) {
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



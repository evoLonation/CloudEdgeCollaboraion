package com.rm2pt.generator.cloudedgecollaboration.generator;


import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.AttributeStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// todo 不要直接生成.sql文件，而是配合MysqlGenerator生成部署文件
public class DDLGenerator{
    private final List<EntityInfo> infoList;
    private final List<EntityStr> DDLEntityList;
    private String DDLText;

    public DDLGenerator(List<EntityInfo> EntityInfoList) {
        infoList = new ArrayList<>();
        infoList.addAll(EntityInfoList);
        DDLText = "";
        DDLEntityList = new ArrayList<>();
    }
    public String getDDLText() {
        return DDLText;
    }

    public void generate() {
        for (EntityInfo entityInfo : infoList) {
            String entityName = entityInfo.getName();

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

//            for (EntityInfo.Association asso : entityInfo.getAssociationList()) {
//                if (!(asso instanceof EntityInfo.ForeignKeyAss)) {
//                    throw new AssertionError("association is not a foreign key!");
//                }
//                if (asso.isMulti()) {
//                    continue;
//                }
//                String assoName = ((EntityInfo.ForeignKeyAss) asso).getRefAttrName();
//                String assoType = ((EntityInfo.ForeignKeyAss) asso).getType().getTypeEnum().name();
//                attributes.add(new AttributeStr(
//                        Keyworder.camelToUnderScore(assoName),
//                        basicType2SQLType(assoType)));
//            }

            EntityStr entityStr = new EntityStr(entityName, primaryKey, attributes);
            DDLEntityList.add(entityStr);
        }
        DDLText = EntityTemplate.SQLContext(DDLEntityList);
    }

    private boolean isInAssociation(EntityInfo entityInfo, EntityInfo.Attribute attribute){
        String attrName = attribute.getName();
        Map<String, EntityInfo.Association> assoMap = entityInfo.getAssociationMap();
        EntityInfo.Association association = assoMap.get(attrName);
        return Objects.nonNull(association);
    }

    private String basicType2SQLType(String type) {
        switch (type) {
            case "INTEGER":
                return "INT64";
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



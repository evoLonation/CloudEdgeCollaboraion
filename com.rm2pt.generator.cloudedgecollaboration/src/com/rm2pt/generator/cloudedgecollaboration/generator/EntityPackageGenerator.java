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

public class EntityPackageGenerator extends Generator {
    private final List<EntityInfo> infoList;
    private final List<EntityStr> GOEntityList;
    private String GOText;

    public EntityPackageGenerator(List<EntityInfo> EntityInfoList) {
        infoList = new ArrayList<>();
        infoList.addAll(EntityInfoList);
        GOEntityList = new ArrayList<>();
    }

    @Override
    public void generate() {
        for (EntityInfo entityInfo : infoList) {
            String entityName = Keyworder.firstUpperCase(entityInfo.getName());

            String idName = entityInfo.getIdAttribute().getName();
            String idType = entityInfo.getIdAttribute().getType().getTypeEnum().name();
            AttributeStr primaryKey = new AttributeStr(
                    Keyworder.firstUpperCase(idName),
                    basicType2GOType(idType),
                    Keyworder.camelToUnderScore(idName));

            List<AttributeStr> attributes = new ArrayList<>();
            for (EntityInfo.Attribute attr : entityInfo.getAttributeList()) {
                if (attr.getName().equals(idName) || isInAssociation(entityInfo, attr)){
                    continue;
                }
                String attrName = attr.getName();
                String attrType = attr.getType().getTypeEnum().name();
                attributes.add(new AttributeStr(
                        Keyworder.firstUpperCase(attrName),
                        basicType2GOType(attrType),
                        Keyworder.camelToUnderScore(attrName)));
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
                        Keyworder.firstUpperCase(assoName),
                        basicType2GOType(assoType),
                        Keyworder.camelToUnderScore(assoName)));
            }

            EntityStr entityStr = new EntityStr(entityName, primaryKey, attributes);
            GOEntityList.add(entityStr);
        }
        GOText = EntityTemplate.GOContext(GOEntityList);
        generateFile("entity/entity.go", GOText);
        System.out.println("Generated_GO");
    }

    private boolean isInAssociation(EntityInfo entityInfo, EntityInfo.Attribute attribute){
        String attrName = attribute.getName();
        Map<String, EntityInfo.Association> assoMap = entityInfo.getAssociationMap();
        EntityInfo.Association association = assoMap.get(attrName);
        return Objects.nonNull(association);
    }

    private String basicType2GOType(String type) {
        switch (type) {
            case "INTEGER":
                return "int64";
            case "TIME":
                return "time.Time";
            case "REAL":
                return "float64";
            case "STRING":
                return "string";
            case "BOOLEAN":
                return "bool";
            default:
                throw new RuntimeException("Unknown entityType: " + type);

        }
    }
}

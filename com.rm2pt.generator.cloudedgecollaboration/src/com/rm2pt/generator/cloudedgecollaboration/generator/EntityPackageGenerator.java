package com.rm2pt.generator.cloudedgecollaboration.generator;


import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.AttributeStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityStr;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.ArrayList;
import java.util.List;

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
    public void generate(){
        for (EntityInfo entityInfo : infoList) {
            String entityName = entityInfo.getName();

            String idName = entityInfo.getId().getName();
            String idType = entityInfo.getId().getType().getName();
            AttributeStr primaryKey = new AttributeStr(
                    Keyworder.firstUpperCase(idName),
                    entityType2GOType(idType),
                    Keyworder.camelToUnderScore(idName));

            List<AttributeStr> attributes = new ArrayList<>();
            for (Variable attr : entityInfo.getAttributeList()){
                String attrName = attr.getName();
                String attrType = attr.getType().getName();
                attributes.add(new AttributeStr(
                        Keyworder.firstUpperCase(attrName),
                        entityType2GOType(attrType),
                        Keyworder.camelToUnderScore(attrName)));
            }

            EntityStr entityStr = new EntityStr(entityName, primaryKey, attributes);
            GOEntityList.add(entityStr);
        }
        GOText = EntityTemplate.GOContext(GOEntityList);
        generateFile("entity.go", GOText);
        System.out.println("Generated_GO");
    }

    private String entityType2GOType(String type) {
        switch (type) {
            case "INTEGER":
                return "int32";
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

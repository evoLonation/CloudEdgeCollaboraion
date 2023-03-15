package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType.TypeEnum;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import net.mydreamy.requirementmodel.rEMODEL.Attribute;
import net.mydreamy.requirementmodel.rEMODEL.Entity;
import net.mydreamy.requirementmodel.rEMODEL.Invariance;
import net.mydreamy.requirementmodel.rEMODEL.Reference;
import net.mydreamy.requirementmodel.rEMODEL.impl.EnumEntityImpl;
import net.mydreamy.requirementmodel.rEMODEL.impl.PrimitiveTypeCSImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于将需求模型的Entity转换为EntityInfo
 */
public class EntityFactory {
    private final List<Entity> entityList;
    private final List<EntityInfo> infoList;

    public EntityFactory(List<Entity> entityList) {
        this.entityList = new ArrayList<>();
        this.entityList.addAll(entityList);
        this.infoList = new ArrayList<>();
    }

    public void factory() {
        if (entityList.size() == 0) {
            throw new RuntimeException("There is no entity in the model!");
        }
        for (Entity entity : entityList) {
            String name = entity.getName();
            List<Attribute> attrs = entity.getAttributes();
            List<Invariance> invs = entity.getInvariance();
            EntityInfo.Attribute id = getUniqueId(attrs, invs);
            EntityInfo.StorageType type = getStorageType(attrs);
            List<EntityInfo.Attribute> attributes = new ArrayList<>();
            for (Attribute attr : attrs) {
                String attrName = attr.getName();
                if (attrName.equals(id.getName()) || attrName.equals("Type")) { // id and storageType are not attr
                    continue;
                }
                attributes.add(attr2InfoAttr(attr));
            }

            EntityInfo info = new EntityInfo();
            info.setIdAttribute(id);
            info.setName(name);
            info.setAttributeList(attributes);
            info.setStorageType(type);
            if (type.equals(EntityInfo.StorageType.EDGE)){ // add EdgeId
                EntityInfo.Attribute edgeId = new EntityInfo.Attribute();
                edgeId.setName("EdgeId");
                BasicType edgeIdType = new BasicType();
                edgeIdType.setTypeEnum(TypeEnum.INTEGER);
                edgeId.setType(edgeIdType);
                info.addAttribute(edgeId);
            }
            this.infoList.add(info);
        }
        addRef2Association();
        generateMaps();
//        outputEntityInfoListForDebug();
    }

    private void outputEntityInfoListForDebug() { //if there is any problem with EntityInfo, use this to debug
        for (EntityInfo entityInfo : infoList) {
            System.out.println();
            System.out.println("EntityName: " + entityInfo.getName());
            System.out.println("idName: " + entityInfo.getIdAttribute().getName());
            System.out.println("idType: "+entityInfo.getIdAttribute().getType().getTypeEnum().name());
            System.out.println("storageType: " + entityInfo.getStorageType().name());
            System.out.println("Attributes:");
            List<EntityInfo.Attribute> attrs = entityInfo.getAttributeList();
            for (int i = 0; i < attrs.size(); i++) {
                EntityInfo.Attribute attr = attrs.get(i);
                System.out.println("-Attr" + i + ": ");
                System.out.println("--attrName: " + attr.getName());
                System.out.println("--attrType: " + attr.getType().getTypeEnum().name());
            }
            System.out.println("Associations:");
            List<EntityInfo.Association> associations = entityInfo.getAssociationList();
            for (int i = 0; i < associations.size(); i++){
                EntityInfo.Association association = associations.get(i);
                if (!(association instanceof EntityInfo.ForeignKeyAss)){
                    throw new AssertionError("association is not a foreign key!");
                }
                System.out.println("-Asso" + i + ": ");
                System.out.println("--assoName: " + association.getName());
                System.out.println("--assoIsMulti: " + association.isMulti());
                System.out.println("--assoRefEntityName: " + association.getTargetEntity().getName());
                System.out.println("--assoRefName: " + ((EntityInfo.ForeignKeyAss) association).getRefAttrName());
                System.out.println("--assoRefType: " + ((EntityInfo.ForeignKeyAss) association).getType().getTypeEnum().name());
            }
        }
    }

    private void generateMaps(){
        for (EntityInfo entityInfo : infoList){
            Map<String, EntityInfo.Attribute> attrMap = new HashMap<>();
            Map<String, EntityInfo.Association> assoMap = new HashMap<>();
            Map<String, EntityInfo.KeyType> keyTypeMap = new HashMap<>();

            for (EntityInfo.Attribute attribute : entityInfo.getAttributeList()){
                attrMap.put(attribute.getName(), attribute);
                keyTypeMap.put(attribute.getName(), EntityInfo.KeyType.ATTRIBUTE);
            }
            for (EntityInfo.Association association : entityInfo.getAssociationList()){
                assoMap.put(association.getName(), association);
                keyTypeMap.put(association.getName(), EntityInfo.KeyType.ASSOCIATION);
            }
        }
    }

    private void addRef2Association() {
        for (Entity entity : entityList) {
            List<Reference> refs = entity.getReference();
            EntityInfo matchedInfo = EntityMatchEntityInfo(entity);
            List<EntityInfo.Association> associationList = new ArrayList<>();
            for (Reference ref : refs) {
                Entity refEntity = ref.getEntity();
                EntityInfo matchedRefInfo = EntityMatchEntityInfo(refEntity);
                EntityInfo.ForeignKeyAss association = new EntityInfo.ForeignKeyAss();
                association.setName(ref.getName() + matchedRefInfo.getIdAttribute().getName());
                association.setType(matchedRefInfo.getIdAttribute().getType());
                association.setMulti(ref.isIsmultiple());
                association.setTargetEntity(matchedRefInfo);
                association.setRefAttrName(matchedRefInfo.getIdAttribute().getName());
                associationList.add(association);
            }
            matchedInfo.setAssociationList(associationList);
        }
    }

    private EntityInfo EntityMatchEntityInfo(Entity entity){ // find the entityInfo with the same name
        for (EntityInfo entityInfo : infoList){
            if (entity.getName().equals(entityInfo.getName())){
                return entityInfo;
            }
        }
        throw new RuntimeException("No matched EntityInfo to: " + entity.getName());
    }

    private EntityInfo.StorageType getStorageType(List<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("Type")) {
                if (!(attr.getType() instanceof EnumEntityImpl)) {
                    throw new RuntimeException("Type attr is not EnumEntityImpl");
                }
                switch (((EnumEntityImpl) attr.getType()).getName()) {
                    case "Cloud":
                        return EntityInfo.StorageType.CLOUD;
                    case "Cache":
                        return EntityInfo.StorageType.CACHE;
                    case "Edge":
                        return EntityInfo.StorageType.EDGE;
                    default:
                        throw new RuntimeException("Unknown StorageType: " + ((EnumEntityImpl) attr.getType()).getName());
                }
            }
        }
        throw new RuntimeException("No StorageType found");
    }

    private EntityInfo.Attribute getUniqueId(List<Attribute> attrs, List<Invariance> invs) { // UniqueId is guaranteed to be like "UniqueXXX", and XXX must be an attribute
        EntityInfo.Attribute id = new EntityInfo.Attribute();
        String idName = null;
        BasicType idType = new BasicType();
        for (Invariance inv : invs) {
            String invName = inv.getName();
            if (invName.contains("Unique")) {
                idName = invName.replaceFirst("Unique", "");
                break;
            }
        }
        if (idName == null) { // no id, create a "GenerateId" to id, this is guaranteed not to be a non-multiple ref
            idName = "GenerateId";
            idType.setTypeEnum(TypeEnum.INTEGER);
        }else {
            for (Attribute attr : attrs){
                if (!(attr.getType() instanceof PrimitiveTypeCSImpl)){
                    continue;
                }
                EntityInfo.Attribute attribute = attr2InfoAttr(attr);
                if (attribute.getName().equals(idName)){
                    idType = attribute.getType();
                }
            }
            if (idType == null){
                throw new RuntimeException("UniqueXXX is not an attribute of entity: " + idName);
            }
        }
        id.setName(idName);
        id.setType(idType);
        return id;
    }

    private EntityInfo.Attribute attr2InfoAttr(Attribute attr) {
        EntityInfo.Attribute attribute = new EntityInfo.Attribute();
        String name = attr.getName();
        BasicType type = new BasicType();
        if (!(attr.getType() instanceof PrimitiveTypeCSImpl)) {
            throw new RuntimeException("Attribute is not PrimitiveTypeCSImpl");
        }
        switch (((PrimitiveTypeCSImpl) attr.getType()).getName()) {
            case "Real":
                type.setTypeEnum(TypeEnum.REAL);
                break;
            case "Integer":
                type.setTypeEnum(TypeEnum.INTEGER);
                break;
            case "Date":
                type.setName(BasicType.TypeEnum.TIME.name());
                type.setTypeEnum(TypeEnum.TIME);
                break;
            case "Boolean":
                type.setTypeEnum(TypeEnum.BOOLEAN);
                break;
            case "String":
                type.setTypeEnum(TypeEnum.STRING);
                break;
            default:
                throw new RuntimeException("Unknown BasicType: " + ((PrimitiveTypeCSImpl) attr.getType()).getName());

        }
        attribute.setName(name);
        attribute.setType(type);
        return attribute;
    }

    /**
     * @return 一个Entity表，用于通过Entity的名称检索到EntityInfo
     */
    public Map<String, EntityInfo> getEntityMap() {
        Map<String, EntityInfo>map = new HashMap<>();
        for (EntityInfo entityInfo : infoList){
            map.put(entityInfo.getName(), entityInfo);
        }
        return map;
    }

    /**
     * @return 返回所有的EntityInfo
     */
    public List<EntityInfo> getEntityList() {
        return infoList;
    }
}

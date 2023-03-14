package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
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
    private List<EntityInfo> infoList;

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
            Variable id = getUniqueId(invs);
            EntityInfo.StorageType type = getStorageType(attrs);
            List<Variable> attrVar = new ArrayList<>();
            for (Attribute attr : attrs) {
                String attrName = attr.getName();
                if (attrName.equals(id.getName()) || attrName.equals("Type")) { // id and storageType are not attr
                    continue;
                }
                attrVar.add(attr2Variable(attr));
            }

            EntityInfo info = new EntityInfo();
            info.setEntity(true);
            info.setId(id);
            info.setName(name);
            info.setAttributeList(attrVar);
            info.setStorageType(type);
            if (type.equals(EntityInfo.StorageType.EDGE)){ // add EdgeId
                Variable edgeId = new Variable();
                edgeId.setName("EdgeId");
                Type edgeIdType = new Type();
                edgeIdType.setName(BasicType.TypeEnum.INTEGER.name());
                edgeIdType.setEntity(false);
                edgeIdType.setMulti(false);
                edgeId.setType(edgeIdType);
                info.addAttribute(edgeId);
            }
            this.infoList.add(info);
        }
        addRef2Attr();
        //outputEntityInfoListForDebug();
    }

    private void outputEntityInfoListForDebug() {
        for (EntityInfo entityInfo : infoList) {
            System.out.println();
            System.out.println("name: " + entityInfo.getName());
            System.out.println("id: " + entityInfo.getId().getName());
            System.out.println("storageType: " + entityInfo.getStorageType().name());
            System.out.println("Attributes:");
            List<Variable> attrs = entityInfo.getAttributeList();
            for (int i = 0; i < attrs.size(); i++) {
                Variable attr = attrs.get(i);
                System.out.println("Attr" + i + ": ");
                System.out.println("attrName: " + attr.getName());
                System.out.println("attrType: " + attr.getType().getName());
            }
        }
    }

    private void addRef2Attr() {
        for (Entity entity : entityList) {
            List<Reference> refs = entity.getReference();
            EntityInfo matchedInfo = EntityMatchEntityInfo(entity);
            for (Reference ref : refs) {
                Entity refEntity = ref.getEntity();
                if (ref.isIsmultiple()) { //multiple is not attr
                    continue;
                }
                EntityInfo matchedRefInfo = EntityMatchEntityInfo(refEntity);
                matchedInfo.addAttribute(matchedRefInfo.getId());
            }
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

    private Variable attr2Variable(Attribute attr) {
        Variable var = new Variable();
        String name = attr.getName();
        Type type = new Type();
        if (!(attr.getType() instanceof PrimitiveTypeCSImpl)) {
            throw new RuntimeException("Attribute is not PrimitiveTypeCSImpl");
        }
        switch (((PrimitiveTypeCSImpl) attr.getType()).getName()) {
            case "Real":
                type.setName(BasicType.TypeEnum.REAL.name());
                break;
            case "Integer":
                type.setName(BasicType.TypeEnum.INTEGER.name());
                break;
            case "Date":
                type.setName(BasicType.TypeEnum.TIME.name());
                break;
            case "Boolean":
                type.setName(BasicType.TypeEnum.BOOLEAN.name());
                break;
            default:
                throw new RuntimeException("Unknown BasicType: " + ((PrimitiveTypeCSImpl) attr.getType()).getName());

        }
        type.setEntity(false);
        type.setMulti(false);
        var.setName(name);
        var.setType(type);
        return var;
    }

    private Variable getUniqueId(List<Invariance> invs) { // UniqueId is guaranteed to be like "UniqueXXX", and XXX must be an attribute
        Variable id = new Variable();
        boolean findId = false;
        for (Invariance inv : invs) {
            String invName = inv.getName();
            if (invName.contains("Unique")) {
                findId = true;
                id.setName(invName.replaceFirst("Unique", ""));
                break;
            }
        }
        if (!findId) { // no id, create a "GenerateId" to id, this is guaranteed not to be a non-multiple ref
            id.setName("GenerateId");
        }
        Type idType = new Type();
        idType.setName(BasicType.TypeEnum.INTEGER.name());
        idType.setEntity(false);
        idType.setMulti(false);
        id.setType(idType);
        return id;
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

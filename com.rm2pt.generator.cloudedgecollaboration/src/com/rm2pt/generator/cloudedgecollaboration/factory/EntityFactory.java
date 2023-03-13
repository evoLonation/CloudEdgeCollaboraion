package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import net.mydreamy.requirementmodel.rEMODEL.Attribute;
import net.mydreamy.requirementmodel.rEMODEL.Entity;
import net.mydreamy.requirementmodel.rEMODEL.Invariance;
import net.mydreamy.requirementmodel.rEMODEL.Reference;
import net.mydreamy.requirementmodel.rEMODEL.impl.EnumEntityImpl;
import net.mydreamy.requirementmodel.rEMODEL.impl.PrimitiveTypeCSImpl;

import java.util.ArrayList;
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

    public void factory(){
        if (entityList.size()==0){
            throw new RuntimeException("There is no entity in the model!");
        }
        for (Entity entity : entityList){
            String name = entity.getName();
            List<Attribute> attrs = entity.getAttributes();
            List<Invariance> invs = entity.getInvariance();
            Variable id = getUniqueId(invs);
            EntityInfo.StorageType type = getStorageType(attrs);
            List<Variable> attrVar = new ArrayList<>();
            for (Attribute attr : attrs){
                String attrName = attr.getName();
                if (attrName.equals(id.getName()) || attrName.equals("Type")){
                    continue;
                }
                attrVar.add(attr2Variable(attr));
            }

            EntityInfo info = new EntityInfo();
            info.setId(id);
            info.setName(name);
            info.setAttributeList(attrVar);
            info.setStorageType(type);
            this.infoList.add(info);
        }
        addRef2Attr();
    }

    private void addRef2Attr(){
        for (Entity entity : entityList){
            List<Reference> refs = entity.getReference();
            System.out.println(refs);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    }

    private EntityInfo.StorageType getStorageType(List<Attribute> attrs){
        for(Attribute attr : attrs){
            if (attr.getName().equals("Type")){
                if (! (attr.getType() instanceof EnumEntityImpl)){
                    throw new RuntimeException("Type attr is not EnumEntityImpl");
                }
                switch (((EnumEntityImpl) attr.getType()).getName()){
                    case "Cloud":
                        return EntityInfo.StorageType.CLOUD;
                    case "Cache":
                        return EntityInfo.StorageType.CACHE;
                    case "Edge":
                        return EntityInfo.StorageType.EDGE;
                    default:
                        throw new RuntimeException("Unknown StorageType: "+((EnumEntityImpl) attr.getType()).getName());
                }
            }
        }
        throw new RuntimeException("No StorageType found");
    }

    private Variable attr2Variable(Attribute attr){
        Variable var = new Variable();
        String name = attr.getName();
        BasicType type = new BasicType(){};
        if (!(attr.getType() instanceof PrimitiveTypeCSImpl)){
            throw new RuntimeException("Attribute is not PrimitiveTypeCSImpl");
        }
        switch (((PrimitiveTypeCSImpl)attr.getType()).getName()){
            case "Real":
                type.setTypeEnum(BasicType.TypeEnum.REAL);break;
            case "Integer":
                type.setTypeEnum(BasicType.TypeEnum.INTEGER);break;
            case "Date":
                type.setTypeEnum(BasicType.TypeEnum.TIME);break;
            case "Boolean":
                type.setTypeEnum(BasicType.TypeEnum.BOOLEAN);break;
            default:
                throw new RuntimeException("Unknown BasicType: "+((PrimitiveTypeCSImpl)attr.getType()).getName());

        }
        var.setName(name);
        var.setType(type);
        return var;
    }

    private Variable getUniqueId(List<Invariance> invs){
        Variable id = new Variable();
        boolean findId = false;
        for(Invariance inv : invs){
            String invName = inv.getName();
            if (invName.contains("Unique")){
                findId = true;
                id.setName(invName.replaceFirst("Unique", ""));
                break;
            }
        }
        if (!findId){ // no id, create a "GenerateId" to id
            id.setName("GenerateId");
        }
        BasicType idType = new BasicType() {};
        idType.setTypeEnum(BasicType.TypeEnum.INTEGER);
        id.setType(idType);
        return id;
    }

    /**
     * @return 一个Entity表，用于通过Entity的名称检索到EntityInfo
     */
    public Map<String, EntityInfo> getEntityMap(){
        throw new UnsupportedOperationException();
    }

    /**
     * @return 返回所有的EntityInfo
     */
    public List<EntityInfo> getEntityList() {
        throw new UnsupportedOperationException();
    }
}

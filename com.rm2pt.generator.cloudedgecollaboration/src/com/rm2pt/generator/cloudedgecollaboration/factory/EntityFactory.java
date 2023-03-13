package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import net.mydreamy.requirementmodel.rEMODEL.Entity;

import java.util.List;
import java.util.Map;

/**
 * 用于将需求模型的Entity转换为EntityInfo
 */
public class EntityFactory {

    public EntityFactory(List<Entity> entityList) {
    }

    public void factory(){

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

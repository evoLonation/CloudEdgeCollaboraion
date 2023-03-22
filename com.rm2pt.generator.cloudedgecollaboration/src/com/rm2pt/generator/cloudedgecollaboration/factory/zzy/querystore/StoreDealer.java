package com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Store;

import java.util.*;
import java.util.stream.Collectors;

public class StoreDealer {
    private List<StoreBuilder> builderList = new ArrayList<>();
    private Map<Variable, StoreBuilder> storeBuilderMap = new HashMap<>();

    /**
     * @param variable entity类型
     * @param attribute 属于variable
     * 当使用了variable的attribute时调用该方法
     */
    public void attributeChanged(Variable variable, EntityInfo.Attribute attribute){
        check(variable.mustGetEntity().isOwner(attribute));
        check(variable.changeable());
        if(storeBuilderMap.containsKey(variable)){
            storeBuilderMap.get(variable).addChangedAttribute(attribute);
        }else{
            var builder = new StoreBuilder(variable);
            builderList.add(builder);
            storeBuilderMap.put(variable, builder);
            builder.addChangedAttribute(attribute);
        }
    }
    public Set<Store> getStores(){
        return builderList.stream().map(StoreBuilder::build).collect(Collectors.toSet());
    }

    public void check(boolean condition){
        if(!condition){
            throw new UnsupportedOperationException();
        }
    }
}

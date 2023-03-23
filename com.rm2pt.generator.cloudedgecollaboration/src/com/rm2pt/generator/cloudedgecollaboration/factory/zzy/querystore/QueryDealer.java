package com.rm2pt.generator.cloudedgecollaboration.factory.zzy.querystore;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AllInstance;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.CollectionOp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class QueryDealer {
    private List<Object> builderList = new ArrayList<>();
    private Map<Variable, GetBuilder> getBuilderMap = new HashMap<>();
    private Map<Variable, SelectBuilder> selectBuilderMap = new HashMap<>();

    public void addCollectionOp(CollectionOp collectionOp) {
        var source = collectionOp.getSource();
        var target = collectionOp.getTargetVar();
        if(source instanceof AllInstance){
            if(collectionOp instanceof Condition){
                var condition = (Condition)collectionOp;
                var logicExp = condition.getLogicExp();
                var internal = condition.getInternalVar();
                if(condition.isReturnMulti()){
                    SelectBuilder selectBuilder = new SelectBuilder(logicExp, internal, target);
                    builderList.add(selectBuilder);
                    selectBuilderMap.put(target, selectBuilder);
                }else{
                    GetBuilder getBuilder = new GetBuilder(logicExp, internal, target);
                    builderList.add(getBuilder);
                    getBuilderMap.put(target, getBuilder);
                }
            }else {
                // todo
                throw new UnsupportedOperationException();
            }
        }else{
            throw new UnsupportedOperationException();
            // todo
        }

    }

    /**
     * @param variable entity类型
     * @param attribute 属于variable
     * 当使用了variable的attribute时调用该方法
     */
    public void attributeUsed(Variable variable, EntityInfo.Attribute attribute){
        check(variable.mustGetEntity().isOwner(attribute));
        if(getBuilderMap.containsKey(variable)){
            getBuilderMap.get(variable).addUsedAttribute(attribute);
        }
        if(selectBuilderMap.containsKey(variable)){
            selectBuilderMap.get(variable).addUsedAttribute(attribute);
        }
    }
    /**
     * @param variable entity类型
     * 当整个variable被引用时（如当作返回值）
     */
    public void allUsed(Variable variable){
        variable.mustGetEntity().getAttributeList().forEach(attribute -> attributeUsed(variable, attribute));
    }

    public List<Query> getQueries(){
        return builderList.stream().map(o -> {
            if(o instanceof SelectBuilder){
                return ((SelectBuilder) o).build();
            }else if(o instanceof GetBuilder){
                return ((GetBuilder) o).build();
            }
            throw new UnsupportedOperationException();
        }).collect(Collectors.toList());
    }

    public void check(boolean condition){
        if(!condition){
            throw new UnsupportedOperationException();
        }
    }
}

package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;


import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AtomicCondition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Sort;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.AttributeValue;
import net.mydreamy.requirementmodel.rEMODEL.ClassiferCallExpCS;
import net.mydreamy.requirementmodel.rEMODEL.PropertyCallExpCS;

public class CallExpDealer extends OperationBodyContext{

    public static class PCValue extends DefinitionFactory.PCRet {
        public AttributeValue attributeValue;
        public PCValue(AttributeValue attributeValue) {
            this.attributeValue = attributeValue;
        }
    }
    public static class PCSelect extends DefinitionFactory.PCRet {
        // 待设置variable
        public SelectBuilder selectBuilder;
        public EntityInfo entityInfo;

        public PCSelect(SelectBuilder selectBuilder, EntityInfo entityInfo) {
            this.selectBuilder = selectBuilder;
            this.entityInfo = entityInfo;
        }
    }
    public abstract static class PCRet{ }

    // 变量的属性，直接转化为value
    // 变量的关联，先转化为select
    public DefinitionFactory.PCRet dealPropertyCall(PropertyCallExpCS propertyCallExp){
        // 被呼叫者
        Variable callee;
        if(propertyCallExp.getSelfproperty() == null){
            callee = getVariable(propertyCallExp.getName());
        }else{
            if(propertyCallExp.getName().getSymbol().equals("self")){
                callee = getVariable(propertyCallExp.getSelfproperty());
            }else{
                // todo
                throw new UnsupportedOperationException();
            }
        }
        EntityInfo entityInfo = ((EntityTypeInfo)callee.getType()).getEntityInfo();
        String key = propertyCallExp.getAttribute();
        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
        // 例如：patient.Temperatures_timestamp_10，指按timestamp降序取前10个
        String[] subKeys = key.split("_");
        String realKey = subKeys[0];
        switch (entityInfo.getKeyType(realKey)){
            case ATTRIBUTE:
                EntityInfo.Attribute attribute = entityInfo.getAttribute(key);
                return new DefinitionFactory.PCValue(new AttributeValue(callee, attribute.getName()));
            case ASSOCIATION:
                EntityInfo.Association association = entityInfo.getAssociation(key);

                SelectBuilder selectBuilder = new SelectBuilder();
                selectBuilder.setLocation(serviceInfo.getLocation());

                if(association instanceof EntityInfo.ForeignKeyAss){
                    EntityInfo.ForeignKeyAss foreignKeyAss = (EntityInfo.ForeignKeyAss) association;
                    EntityInfo targetEntity = foreignKeyAss.getTargetEntity();
                    Condition condition;
                    if(foreignKeyAss.isMulti()){
                        // 外键字段在对方
                        selectBuilder.addCondition(
                                new AtomicCondition(
                                        foreignKeyAss.getRefAttrName(),
                                        new AttributeValue(callee, entityInfo.getIdAttribute().getName()),
                                        AtomicCondition.OP.EQUALS
                                )
                        );
                        // todo 无奈之举，通过key后面加下划线来模拟subList和sort
                        if(subKeys.length >= 2){
                            selectBuilder.addSort(new Sort(subKeys[1], Sort.Rule.DESCENDING));
                            if(subKeys.length >= 3){
                                selectBuilder.setLimit(Integer.parseInt(subKeys[2]));
                            }
                        }

                    }else{
                        // 外键字段在己方
                        selectBuilder.addCondition(
                                new AtomicCondition(
                                        targetEntity.getIdAttribute().getName(),
                                        new AttributeValue(callee, foreignKeyAss.getName()),
                                        AtomicCondition.OP.EQUALS
                                )
                        );
                        // todo 外键字段在对方?
                    }
                }else {
                    // todo 多-多关联
                    throw new UnsupportedOperationException();
                }
                return new DefinitionFactory.PCSelect(selectBuilder, association.getTargetEntity());
        }
        throw new UnsupportedOperationException();
    }


    public static class CCSelect{
        public SelectBuilder selectBuilder;
        public EntityInfo entityInfo;

        public CCSelect(SelectBuilder selectBuilder, EntityInfo entityInfo) {
            this.selectBuilder = selectBuilder;
            this.entityInfo = entityInfo;
        }
    }

    // 判断entityInfo
    // 只能是allInstance()
    public DefinitionFactory.CCSelect dealClassiferCall(ClassiferCallExpCS classiferCallExp){
        SelectBuilder selectBuilder = new SelectBuilder();
        selectBuilder.setLocation(serviceInfo.getLocation());
        check(classiferCallExp.getOp().equals("allInstance()"));
        return new DefinitionFactory.CCSelect(selectBuilder, entityInfoMap.get(classiferCallExp.getEntity()));
    }
}

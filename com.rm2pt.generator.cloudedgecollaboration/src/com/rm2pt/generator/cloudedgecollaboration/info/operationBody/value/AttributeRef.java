package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class AttributeRef extends UnaryValue {
    private Variable variable;
    private EntityInfo.Attribute attribute;

    public AttributeRef(Variable variable, EntityInfo.Attribute attribute) {
        this.variable = variable;
        this.attribute = attribute;
        check();
    }

    private void check(){
        if(variable.getType() instanceof EntityTypeInfo){
            if(((EntityTypeInfo) variable.getType()).getEntityInfo().getKeyType(attribute.getName()) == EntityInfo.KeyType.ATTRIBUTE){
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    public Variable getVariable() {
        return variable;
    }

    public EntityInfo.Attribute getAttribute() {
        return attribute;
    }

    @Override
    public BasicType getType() {
        return attribute.getType();
    }
}

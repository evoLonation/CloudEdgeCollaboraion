package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

public class Collection extends Source {
    private Variable variable;

    public Variable getVariable() {
        return variable;
    }
    public void check(){
        if(variable.mustGetEntity() != null){
            if(variable.getType().isMulti()){
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    public Collection(Variable variable) {
        this.variable = variable;
        check();
    }


    @Override
    public EntityInfo getEntityInfo() {
        return variable.mustGetEntity();
    }
}

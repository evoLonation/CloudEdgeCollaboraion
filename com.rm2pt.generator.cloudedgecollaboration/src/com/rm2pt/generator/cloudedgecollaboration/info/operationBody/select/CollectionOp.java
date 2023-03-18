package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;

public abstract class CollectionOp extends Statement {
    protected Variable targetVar;
    protected Source source;

    public CollectionOp(Variable targetVar, Source source) {
        this.targetVar = targetVar;
        this.source = source;
        check();
    }

    private  void check(){
        if(targetVar.mustGetEntity().equals(source.getEntityInfo())){
            if(targetVar.getType().isMulti() == isReturnMulti()){
                return;
            }
        }
        throw new UnsupportedOperationException();
    }

    protected boolean isReturnMulti(){
        return true;
    }
}

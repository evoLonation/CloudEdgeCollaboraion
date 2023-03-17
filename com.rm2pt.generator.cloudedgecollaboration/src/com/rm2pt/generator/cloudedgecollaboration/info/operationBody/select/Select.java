package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;

public class Select extends Statement {
    private Variable targetVar;
    private CollectionOp collectionOp;
    private Source source;

    public Select(Variable targetVar, CollectionOp collectionOp, Source source) {
        this.targetVar = targetVar;
        this.collectionOp = collectionOp;
        this.source = source;
    }
    private void check(){
        if(targetVar.getType().isMulti() && targetVar.mustGetEntity().equals(source.getEntityInfo())){
            return;
        }
        throw new UnsupportedOperationException();
    }

    public Variable getTargetVar() {
        return targetVar;
    }

    public CollectionOp getCollectionOp() {
        return collectionOp;
    }

    public Source getSource() {
        return source;
    }
}

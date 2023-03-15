package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition.Exp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Select;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Store;

import java.util.List;

public class OperationBody {

    // 别忘了把result加进去
    private List<Select> selectList;
    private Exp expression;

    private List<Statement> statementList;

    private List<Store> storeList;

    public void setSelectList(List<Select> selectList) {
        this.selectList = selectList;
    }

    public void setExpression(Exp expression) {
        this.expression = expression;
    }

    public void setStatementList(List<Statement> statementList) {
        this.statementList = statementList;
    }

    public void setStoreList(List<Store> storeList) {
        this.storeList = storeList;
    }
}


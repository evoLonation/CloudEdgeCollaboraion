package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.UnaryValue;

import java.util.List;

public class ThirdParty extends Statement{
    private String name;
    private List<UnaryValue> paramList;

    public ThirdParty(String name, List<UnaryValue> paramList) {
        this.name = name;
        this.paramList = paramList;
    }

    public String getName() {
        return name;
    }

    public List<UnaryValue> getParamList() {
        return paramList;
    }
}

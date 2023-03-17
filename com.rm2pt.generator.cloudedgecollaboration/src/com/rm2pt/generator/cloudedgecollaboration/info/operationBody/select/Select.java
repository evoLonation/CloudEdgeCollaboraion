package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.VariableValue;

import java.util.ArrayList;
import java.util.List;

public class Select extends Statement {
    private VariableValue variableValue;
    private List<String> selectedAttribute = new ArrayList<>();
    private Condition condition;
    private List<Sort> sortList;
    private Limit limit;

    private Location location;

    public Select(VariableValue variableValue, Condition condition, List<Sort> sortList, Limit limit, Location location) {
        this.variableValue = variableValue;
        this.condition = condition;
        this.sortList = sortList;
        this.limit = limit;
        this.location = location;
    }

    public VariableValue getVariableValue() {
        return variableValue;
    }

    public List<String> getSelectedAttribute() {
        return selectedAttribute;
    }

    public Condition getCondition() {
        return condition;
    }


    public List<Sort> getSortList() {
        return sortList;
    }

    public Limit getLimit() {
        return limit;
    }

    public Location getLocation() {
        return location;
    }
}

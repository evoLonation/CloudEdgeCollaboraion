package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.Statement;

import java.util.ArrayList;
import java.util.List;

public class Select extends Statement {
    private Variable variable;
    private List<String> selectedAttribute = new ArrayList<>();
    private Condition condition;
    private List<Sort> sortList;
    private Integer limit;

    private Location location;

    public Select(Variable variable, Condition condition, List<Sort> sortList, Integer limit, Location location) {
        this.variable = variable;
        this.condition = condition;
        this.sortList = sortList;
        this.limit = limit;
        this.location = location;
    }

    public Variable getVariable() {
        return variable;
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

    public Integer getLimit() {
        return limit;
    }

    public Location getLocation() {
        return location;
    }
}

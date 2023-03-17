package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.BinaryCondition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Select;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Sort;

import java.util.ArrayList;
import java.util.List;

public class SelectBuilder {
    private Variable variable;
    private List<String> selectedAttribute = new ArrayList<>();
    private Condition condition;
    private List<Sort> sortList = new ArrayList<>();
    private Integer limit;
    private Location location;

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void addCondition(Condition condition) {
        if(this.condition == null){
            this.condition = condition;
        }else{
            this.condition = new BinaryCondition(this.condition, condition, BinaryCondition.OP.AND);
        }
    }

    public void addSelectedAttribute(String attribute){
        selectedAttribute.add(attribute);
    }
    public void addSort(Sort sort){
        sortList.add(sort);
    }

    public Select build(){
        if(variable == null || condition == null || location == null){
            throw new UnsupportedOperationException();
        }
        return new Select(variable, condition, sortList, limit, location);
    }
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class OperationBody {

    private List<Statement> statementList = new ArrayList<>();

    private Location location;

    public void addStatement(Statement statement){
        statementList.add(statement);
    }

    public Location getLocation() {
        return location;
    }

    public void setStatementList(List<Statement> statementList) {
        this.statementList = statementList;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}


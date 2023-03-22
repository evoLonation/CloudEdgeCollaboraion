package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;

import java.util.List;

public class OperationBody {

    private List<Statement> statementList;

    private Location location;

    public OperationBody(List<Statement> statementList, Location location) {
        this.statementList = statementList;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }


    public List<Statement> getStatementList() {
        return statementList;
    }
}


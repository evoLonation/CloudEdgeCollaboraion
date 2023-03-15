package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;

import java.util.ArrayList;
import java.util.List;

public class OperationBody {

    private List<Statement> statementList = new ArrayList<>();

    private Location location;

    public OperationBody(Location location) {
        this.location = location;
    }
    public void addStatement(Statement statement){
        statementList.add(statement);
    }
}

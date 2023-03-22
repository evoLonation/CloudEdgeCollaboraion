package com.rm2pt.generator.cloudedgecollaboration.info.operationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.postcondition.PostconditionCode;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Query;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.PreCondition;

import java.util.List;

public class OperationBody {

    // todo 计算拓扑关系
    private List<Query> queryList;

    private PreCondition preCondition;

    private PostconditionCode postcondition;

    private Location location;

    public OperationBody(List<Query> queryList, PreCondition preCondition, PostconditionCode postcondition, Location location) {
        this.queryList = queryList;
        this.preCondition = preCondition;
        this.postcondition = postcondition;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public List<Query> getQueryList() {
        return queryList;
    }

    public PreCondition getPreCondition() {
        return preCondition;
    }

    public PostconditionCode getPostcondition() {
        return postcondition;
    }
}


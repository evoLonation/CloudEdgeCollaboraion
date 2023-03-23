package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.postcondition;

import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Assign;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.store.Store;

import java.util.List;
import java.util.Set;

public class Sequence extends PostconditionCode {
    private List<Assign> assignList;
    private Set<Store> storeSet;

    public Sequence(List<Assign> assignList, Set<Store> storeSet) {
        this.assignList = assignList;
        this.storeSet = storeSet;
    }

    public List<Assign> getAssignList() {
        return assignList;
    }

    public Set<Store> getStoreSet() {
        return storeSet;
    }
}

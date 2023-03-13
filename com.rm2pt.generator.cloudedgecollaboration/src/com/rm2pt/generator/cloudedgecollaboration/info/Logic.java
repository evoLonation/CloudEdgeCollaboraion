package com.rm2pt.generator.cloudedgecollaboration.info;


import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;

public class Logic {
    private Caller caller;
    public enum Caller{
        USER,
        NODE;
    }
    private String name;
    private List<Variable> inputParamList;
    // Nullable
    private Type returnType;
    private LogicBody logicBody;
}

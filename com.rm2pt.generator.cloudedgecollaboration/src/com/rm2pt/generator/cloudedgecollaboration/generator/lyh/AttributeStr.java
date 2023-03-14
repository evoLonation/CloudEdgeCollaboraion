package com.rm2pt.generator.cloudedgecollaboration.generator.lyh;

//Attribute's String Format, for SQL and GO output
public class AttributeStr {
    private final String name;
    private final String type;
    private final String SQLName;

    public AttributeStr(String name, String type) { //for SQL generation
        this.name = name;
        this.type = type;
        this.SQLName = null;
    }

    public AttributeStr(String name, String type, String SQLName) { // for GO generation
        this.name = name;
        this.type = type;
        this.SQLName = SQLName;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSQLName() {
        return SQLName;
    }

}
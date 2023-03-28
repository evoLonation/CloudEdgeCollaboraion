package com.rm2pt.generator.cloudedgecollaboration.generator.lyh;

//Attribute's String Format, for SQL and GO output
public class AttributeStr {
    private final String name;
    private final String type;
    private final String SQLName;
    private final String defaultValue; // for SQL default

    public AttributeStr(String name, String type) { //for SQL generation
        this.name = name;
        this.type = type;
        this.SQLName = null;
        defaultValue = getDefaultValue(type);
    }

    public AttributeStr(String name, String type, String SQLName) { // for GO generation
        this.name = name;
        this.type = type;
        this.SQLName = SQLName;
        this.defaultValue = null;
    }

    private static String getDefaultValue(String type){
        switch (type){
            case "BIGINT":
                return "0";
            case "DATETIME":
                return "'0001-01-01 00:00:00'";
            case "DOUBLE":
                return "0";
            case "VARCHAR(255)":
                return "''";
            case "BOOL":
                return "FALSE";
            default:
                throw new RuntimeException("Unknown SQLType: " + type);
        }
    }

    public String getDefaultValue() {
        return defaultValue;
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
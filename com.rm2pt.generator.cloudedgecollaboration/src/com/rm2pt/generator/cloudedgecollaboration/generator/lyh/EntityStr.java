package com.rm2pt.generator.cloudedgecollaboration.generator.lyh;

import java.util.List;

//EntityInfo's String Format, for SQL and GO output
public class EntityStr {

    private final String name;
    private final AttributeStr primaryKey;
    private final List<AttributeStr> attributes;

    public EntityStr(String name, AttributeStr primaryKey, List<AttributeStr> attributes) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public AttributeStr getPrimaryKey() {
        return primaryKey;
    }

    public List<AttributeStr> getAttributes() {
        return attributes;
    }
}


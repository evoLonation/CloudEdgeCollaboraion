package com.rm2pt.generator.cloudedgecollaboration.generator.common;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;

public class TypeGenerator {
    static public String generateGoType(Type type){
        if(type instanceof BasicType){
            var typeEnum = ((BasicType) type).getTypeEnum();
            switch (typeEnum){
                case REAL: return "float64";
                case TIME: return "time.Time";
                case STRING: return "string";
                case BOOLEAN: return "bool";
                case INTEGER: return "int64";
                default:throw new UnsupportedOperationException();
            }
        } else {
            return "entity." + Keyworder.firstUpperCase(((EntityTypeInfo) type).getEntityInfo().getName());
        }
    }
}

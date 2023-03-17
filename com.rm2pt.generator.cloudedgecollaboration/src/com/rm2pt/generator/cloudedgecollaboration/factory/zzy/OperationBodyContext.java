package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import net.mydreamy.requirementmodel.rEMODEL.EntityType;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveTypeCS;
import net.mydreamy.requirementmodel.rEMODEL.TypeCS;

import java.util.Map;

public class OperationBodyContext {
    static protected VariableTable variableTable;
    static protected Map<String, EntityInfo> entityInfoMap;
    static protected OperationBody operationBody;
    static protected ServiceInfo serviceInfo;
    static protected void check(boolean condition){
        if(!condition){
            throw new UnsupportedOperationException();
        }
    }

    static protected Type getType(TypeCS typeCS){
        if(typeCS instanceof EntityType){
            return new EntityTypeInfo(entityInfoMap.get(((EntityType) typeCS).getEntity().getName()));
        }else if(typeCS instanceof PrimitiveTypeCS){
            BasicType.TypeEnum typeEnum;
            switch (((PrimitiveTypeCS) typeCS).getName()){
                case  "Boolean" : typeEnum = BasicType.TypeEnum.BOOLEAN; break;
                case  "String" : typeEnum = BasicType.TypeEnum.STRING; break;
                case "Real" : typeEnum = BasicType.TypeEnum.REAL;break;
                case "Integer" : typeEnum = BasicType.TypeEnum.INTEGER;break;
                case "Date" : typeEnum = BasicType.TypeEnum.TIME;break;
                default: throw new UnsupportedOperationException();
            }
            return new BasicType(typeEnum);
        }else{
            throw new UnsupportedOperationException();
        }
    }
}

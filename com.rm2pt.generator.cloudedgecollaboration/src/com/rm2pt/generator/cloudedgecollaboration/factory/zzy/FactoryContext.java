package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityTypeInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Type;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.Statement;
import net.mydreamy.requirementmodel.rEMODEL.EntityType;
import net.mydreamy.requirementmodel.rEMODEL.PrimitiveTypeCS;
import net.mydreamy.requirementmodel.rEMODEL.TypeCS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;


/**
 * 注意，该类的所有属性都必须是final的
 */
public class FactoryContext {
    protected final VariableTable variableTable;
    protected final Map<String, EntityInfo> entityInfoMap;
    private final Stack<List<Statement>> statementListStack;

    protected final void addStatement(Statement statement){
        statementListStack.peek().add(statement);
    }
    protected final void pushLayer(){
        statementListStack.push(new ArrayList<>());
    }
    protected final List<Statement> popLayer(){
        return statementListStack.pop();
    }
    protected FactoryContext(InitPack initPack){
        this.variableTable = initPack.variableTable;
        this.entityInfoMap = initPack.entityInfoMap;
        this.statementListStack = initPack.statementListStack;
    }
    protected static class InitPack{
        private final VariableTable variableTable;
        private final Map<String, EntityInfo> entityInfoMap;
        private final Stack<List<Statement>> statementListStack;
        public InitPack(VariableTable variableTable, Map<String, EntityInfo> entityInfoMap, Stack<List<Statement>> statementListStack) {
            this.variableTable = variableTable;
            this.entityInfoMap = entityInfoMap;
            this.statementListStack = statementListStack;
        }
    }
    protected final InitPack getInitPack(){
        return new InitPack(
                variableTable,
                entityInfoMap,
                statementListStack
        );
    }
    protected final void check(boolean condition){
//        if(!condition){
//            throw new UnsupportedOperationException();
//        }
    	return;
    }
    protected final Type getType(TypeCS typeCS){
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

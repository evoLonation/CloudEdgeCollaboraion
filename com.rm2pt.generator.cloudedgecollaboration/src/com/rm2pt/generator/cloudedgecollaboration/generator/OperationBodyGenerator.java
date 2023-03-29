package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.generator.common.TypeGenerator;
import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.OperationBodyTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.Location;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.OperationBody;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.AtomicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.ConnectExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp.LogicExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.precondition.IsUndefinedExp;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.AllInstance;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Collection;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select.Condition;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.statement.*;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.*;
import net.mydreamy.requirementmodel.rEMODEL.IfExpCS;

import java.util.*;
import java.util.logging.Formatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationBodyGenerator {
    private List<Statement> statementList;
    private PreCondition preCondition;
    private List<Condition> conditionList;
    private Map<Variable, Set<String>> useSet;
    private Map<Variable, Set<String>> changeSet;
    private Set<Variable> generateIdSet;
    private Set<Variable> variableSet;
    private boolean isConsistency;
    private List<Assign> assignList;
    private ResultAssign resultAssign;
    private LogicExp ifLogicExp;
    public OperationBodyGenerator(OperationBody operationBody, boolean isConsistency){
        statementList = operationBody.getStatementList();
        this.isConsistency = isConsistency;
        // nullable
        preCondition = statementList.stream().filter(statement -> statement instanceof PreCondition).map(e -> (PreCondition)e).findAny().orElse(null);
        conditionList = statementList.stream().filter(s -> s instanceof Condition).map(s -> (Condition)s).collect(Collectors.toList());
        variableSet = new HashSet<>();
        useSet = new HashMap<>();
        changeSet = new HashMap<>();
        conditionList.forEach(condition -> addUse(condition.getTargetVar(), condition.getSource().getEntityInfo().getIdAttribute()));
//        conditionList.forEach(condition -> addChange(condition.getTargetVar(), condition.getSource().getEntityInfo().getIdAttribute()));
        statementList.forEach(this::dealChangeUse);
        var ifExp = statementList.stream().filter(statement -> statement instanceof IfBlock).map(e -> (IfBlock)e).findAny().orElse(null);
        if(ifExp == null){
            resultAssign = statementList.stream().filter(statement -> statement instanceof ResultAssign).map(e -> (ResultAssign)e).findAny().orElse(null);
            assignList = statementList.stream().filter(e -> e instanceof Assign).map(e -> (Assign)e).collect(Collectors.toList());
            ifLogicExp = null;
        }else {
            resultAssign = ifExp.getIfTrue().stream().filter(statement -> statement instanceof ResultAssign).map(e -> (ResultAssign)e).findAny().orElse(null);
            ifLogicExp = ifExp.getCondition();
            assignList = ifExp.getIfTrue().stream().filter(e -> e instanceof Assign).map(e -> (Assign)e).collect(Collectors.toList());
        }
        useSet.keySet().stream().filter(v -> v.getName().equals(resultAssign.getValue())).findAny().ifPresent(v ->
            useSet.get(v).addAll(v.mustGetEntity().getAttributeList().stream().map(EntityInfo.Attribute::getName).collect(Collectors.toList())));
        changeSet.forEach((v, s) -> {
            if(v.getScopeType() == Variable.ScopeType.LET){
                if(s.stream().noneMatch(a -> a.equals(v.mustGetEntity().getIdAttribute().getName()))){
                    assignList.add(new GenerateIdAssign(new AttributeRef(v, v.mustGetEntity().getIdAttribute())));
                    changeSet.get(v).add(v.mustGetEntity().getIdAttribute().getName());
                }
            }
        });
    }
    private static class GenerateIdAssign extends Assign{
        public GenerateIdAssign(AttributeRef left) {
            super(left, left);
        }
    }
    private void dealChangeUse(Statement statement){
        if(statement instanceof Condition){
            variableSet.add(((Condition) statement).getTargetVar());
        }else if(statement instanceof Assign){
            var assign = (Assign)statement;
            variableSet.add(assign.getLeft().getVariable());
            addChange(assign.getLeft().getVariable(), assign.getLeft().getAttribute());
            var right = assign.getRight();
            if(right instanceof AttributeRef){
                addUse(((AttributeRef) right).getVariable(), ((AttributeRef) right).getAttribute());
            }else if(right instanceof BinaryValue){
                if(((BinaryValue) right).getLeft() instanceof AttributeRef){
                    addUse(((AttributeRef) ((BinaryValue) right).getLeft()).getVariable(),
                            ((AttributeRef) ((BinaryValue) right).getLeft()).getAttribute());
                }
                if(((BinaryValue) right).getRight() instanceof AttributeRef){
                    addUse(((AttributeRef) ((BinaryValue) right).getRight()).getVariable(),
                            ((AttributeRef) ((BinaryValue) right).getRight()).getAttribute());
                }
            }
        }else if(statement instanceof IfBlock){
            ((IfBlock)statement).getIfTrue().forEach(this::dealChangeUse);
        }
    }
    private void addChange(Variable variable, EntityInfo.Attribute attribute){
        if(!changeSet.containsKey(variable)){
            changeSet.put(variable, new HashSet<>());
        }
        changeSet.get(variable).add(attribute.getName());
    }
    private void addUse(Variable variable, EntityInfo.Attribute attribute){
        if(!useSet.containsKey(variable)){
            useSet.put(variable, new HashSet<>());
        }
        useSet.get(variable).add(attribute.getName());
    }
    public String generate() {
        return OperationBodyTemplate.generate(generateMutex(), variableSet.stream().map(this::generateVariable).collect(Collectors.toList()),
                conditionList.stream().map(this::generateCondition).collect(Collectors.toList()),
                generatePrecondition(), generatePostcondition()
                );
    }
    private String generateStore(Variable variable){
        var entityInfo = variable.mustGetEntity();
        var table = entityInfo.getName();
        var attrList = new ArrayList<>(changeSet.get(variable));
        if(variable.getScopeType() == Variable.ScopeType.LET){
            if(entityInfo.getStorageType() == EntityInfo.StorageType.DEFAULT){
            	return OperationBodyTemplate.generateSingleInsert(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else if(entityInfo.getStorageType() == EntityInfo.StorageType.HIGHREAD){
                return OperationBodyTemplate.generateReplicationInsert(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else if(entityInfo.getStorageType() == EntityInfo.StorageType.HIGHSTORE){
                return OperationBodyTemplate.generateShardingInsert(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else{throw new UnsupportedOperationException();}
        }else{
            if(entityInfo.getStorageType() == EntityInfo.StorageType.DEFAULT){
                return OperationBodyTemplate.generateSingleUpdate(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else if(entityInfo.getStorageType() == EntityInfo.StorageType.HIGHREAD){
                return OperationBodyTemplate.generateReplicationUpdate(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else if(entityInfo.getStorageType() == EntityInfo.StorageType.HIGHSTORE){
                return OperationBodyTemplate.generateShardingUpdate(variable.getName(), table, attrList, entityInfo.getIdAttribute().getName());
            }else{throw new UnsupportedOperationException();}
        }
    }
    private String generatePostcondition() {
        var result = resultAssign.getValue();
        if(result.equals("item")){
            result = "*" + result;
        }
    	if(ifLogicExp == null) {
    		return OperationBodyTemplate.generatePostcondition(
                    assignList.stream().map(this::generateAssign).collect(Collectors.toList()),
                    changeSet.keySet().stream().map(this::generateStore).collect(Collectors.toList()),
                    result);
        }else{
            return OperationBodyTemplate.generateIfPostcondition(generateLogicExp(ifLogicExp),
                    assignList.stream().map(this::generateAssign).collect(Collectors.toList()),
                    changeSet.keySet().stream().map(this::generateStore).collect(Collectors.toList()),
                    result);
        }
    }
    public String generateAssign(Assign assign){
        if(assign instanceof GenerateIdAssign){
            return generateRValue(assign.getLeft()) + " = p.generateId()";
        }
        return generateRValue(assign.getLeft()) + " = " + generateRValue(assign.getRight());
    }

    private static class SelectByIdRes{
        public EntityInfo entityInfo;
        public EntityInfo.Attribute id;
        public RValue param;
        public SelectByIdRes(EntityInfo entityInfo, EntityInfo.Attribute id, RValue param) {
            this.entityInfo = entityInfo;
            this.id = id;
            this.param = param;
        }
    }

    private SelectByIdRes mustSelectById(Condition condition){
        var entityInfo = ((AllInstance)condition.getSource()).getEntityInfo();
        var type = entityInfo.getStorageType();
        var logicExp = (AtomicExp)((ConnectExp)condition.getLogicExp()).getLogicExpList().get(0);
        var internal = condition.getInternalVar();
        var leftValue = (AttributeRef)(logicExp).getLeft();
        if(!leftValue.getVariable().equals(internal)){
            throw new UnsupportedOperationException();
        }
        if(!leftValue.getVariable().mustGetEntity().getIdAttribute().equals(leftValue.getAttribute())){
            throw new UnsupportedOperationException();
        }
        var rightValue = logicExp.getRight();
        return new SelectByIdRes(entityInfo, leftValue.getAttribute(), rightValue);
    }

    public String generateCondition(Condition condition){
        var res = mustSelectById(condition);
        var entityInfo = res.entityInfo;
        var type = entityInfo.getStorageType();
        var rightValue = res.param;
        var attrList = new ArrayList<>(useSet.get(condition.getTargetVar()));
        if(type == EntityInfo.StorageType.DEFAULT){
            return OperationBodyTemplate.generateSingleSelect(false, condition.getTargetVar().getName(), entityInfo.getName(),
                    attrList, entityInfo.getIdAttribute().getName(),
                    generateRValue(rightValue));
        }else if(type == EntityInfo.StorageType.HIGHREAD){
            return OperationBodyTemplate.generateReplicationSelect(false, isConsistency, condition.getTargetVar().getName(), entityInfo.getName(),
                    attrList, entityInfo.getIdAttribute().getName(),
                    generateRValue(rightValue));
        }else if(type == EntityInfo.StorageType.HIGHSTORE){
            return OperationBodyTemplate.generateShardingSelect(condition.getTargetVar().getName(), entityInfo.getName(),
                    attrList, entityInfo.getIdAttribute().getName(),
                    generateRValue(rightValue));
        }else{throw new UnsupportedOperationException();}
    }

    public String generateVariable(Variable variable){
        return variable.getName() + " := new(" + TypeGenerator.generateGoType(variable.getType()) + ")";
    }
    public String generatePrecondition(){
        if(preCondition == null){
            return "true";
        }
        return generateLogicExp(preCondition.getLogicExp());
    }
    public String generateLogicExp(LogicExp logicExp){
        if(logicExp instanceof ConnectExp){
            StringBuilder stringBuilder = new StringBuilder(" (");
            for(int i = 0; i < ((ConnectExp) logicExp).getOpList().size(); i ++){
                stringBuilder.append(generateLogicExp(((ConnectExp) logicExp).getLogicExpList().get(i)));
                stringBuilder.append(generateLogicOp(((ConnectExp) logicExp).getOpList().get(i)));
            }
            stringBuilder.append(generateLogicExp(((ConnectExp) logicExp).getLogicExpList().get(((ConnectExp) logicExp).getLogicExpList().size() - 1)));
            stringBuilder.append(" )");
            return stringBuilder.toString();
        }else if(logicExp instanceof AtomicExp){
            var op = ((AtomicExp) logicExp).getOp();
            String opstr;
            switch (op){
                case EQ: opstr = " == "; break;
                case GT: opstr = " > "; break;
                case LT: opstr = " < "; break;
                default:throw new UnsupportedOperationException();
            }
            return generateRValue(((AtomicExp) logicExp).getLeft()) + opstr + generateRValue(((AtomicExp) logicExp).getRight());
        }else if(logicExp instanceof IsUndefinedExp){
            String op = ((IsUndefinedExp) logicExp).isUndefined() ? " == " : " != ";
            return ((IsUndefinedExp) logicExp).getVariable().getName() + op + "nil";
        }else {throw new UnsupportedOperationException();}
    }
    public String generateLogicOp(ConnectExp.OP op){
        switch (op){
            case AND: return " && ";
            case OR: return "||";
            default:throw new UnsupportedOperationException();
        }
    }
    public String generateRValue(RValue rValue){
        if(rValue instanceof BinaryValue){
            var op = ((BinaryValue) rValue).getOp();
            String opStr;
            switch (op){
                case ADD: opStr = " + "; break;
                case MULT: opStr = " * "; break;
                case SUB: opStr = " - "; break;
                default:throw new UnsupportedOperationException();
            }
            return generateRValue(((BinaryValue)rValue).getLeft()) + opStr + generateRValue(((BinaryValue)rValue).getRight());
        }else if(rValue instanceof LiteralValue){
            return ((LiteralValue) rValue).getValue();
        }else if(rValue instanceof AttributeRef){
            return ((AttributeRef) rValue).getVariable().getName() + "." + ((AttributeRef) rValue).getAttribute().getName();
        } else if (rValue instanceof BasicVariable) {
            return ((BasicVariable)rValue).getVariable().getName();
        } else if(rValue instanceof CastFloat){
            return "float64(" + generateRValue(((CastFloat)rValue).getrValue()) + ")";
        }else{
            throw new UnsupportedOperationException();
        }
    }

    private List<String> generateMutex(){
        return Stream.concat(
                conditionList.stream().flatMap(condition -> {
                    var res = mustSelectById(condition);
                    var param = generateRValue(res.param);
                    var target = condition.getTargetVar();
                    var entity = res.entityInfo.getName().toLowerCase();
                    Set<String> ret = new HashSet<>(2);
                    if (useSet.containsKey(target)) {
                        ret.add(String.format("\"%s\" + fmt.Sprint(%s) + \"read\"", entity, param));
                    }
                    if (changeSet.containsKey(target)) {
                        ret.add(String.format("\"%s\" + fmt.Sprint(%s) + \"write\"", entity, param));
                        ret.add(String.format("\"%s\" + fmt.Sprint(%s) + \"read\"", entity, param));
                    }
                    return ret.stream();
                }),
                changeSet.entrySet().stream().flatMap(e -> {
                    var v = e.getKey();
                    if (v.getScopeType() != Variable.ScopeType.LET) {
                        return Stream.empty();
                    }
                    var idAssign = assignList.stream().filter(a ->
                                    a.getLeft().getVariable().equals(v) &&
                                            a.getLeft().getAttribute().equals(v.mustGetEntity().getIdAttribute()))
                            .findAny().get();
                    if(idAssign.getLeft().getAttribute().getName().equals("GenerateId")){
                        return Stream.empty();
                    }
                    return Stream.of(String.format("\"%s\" + fmt.Sprint(%s) + \"write\"",
                            idAssign.getLeft().getVariable().mustGetEntity().getName().toLowerCase(),
                            generateRValue(idAssign.getRight())),
                            String.format("\"%s\" + fmt.Sprint(%s) + \"read\"",
                                    idAssign.getLeft().getVariable().mustGetEntity().getName().toLowerCase(),
                                    generateRValue(idAssign.getRight()))
                            );
                })).distinct().collect(Collectors.toList());

    }



    public String generateHighPriority(){
        return "return";
//    	return OperationBodyTemplate.generate(variableSet.stream().map(this::generateVariable).collect(Collectors.toList()),
//                conditionList.stream().map(this::generateCondition).collect(Collectors.toList()),
//                generatePrecondition(), "postcondition");
    }
}

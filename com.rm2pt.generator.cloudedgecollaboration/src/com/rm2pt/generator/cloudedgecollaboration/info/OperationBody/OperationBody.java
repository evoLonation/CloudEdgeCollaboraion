package com.rm2pt.generator.cloudedgecollaboration.info.OperationBody;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;

import java.util.List;
import java.util.Map;

public class OperationBody {

    // 别忘了把result加进去
    private List<Select> selectList;
    private Exp expression;

    private List<Statement> statementList;

    private List<Store> storeList;
}
class VariableTable {
    public enum VariableType{
        PARAM,
        GLOBAL,
        DEFINITION,
        LET;
    }

    private Map<String, Variable> localVariableMap;
    private Map<String, Variable> globalVariableMap;
    private Map<Variable, VariableType> variableTypeMap;
    private Map<Variable, List<String>> variableChangedMap;
    private Map<Variable, List<String>> variableUsedMap;
}

class Select{
    private Variable variable;
    private List<String> selectedAttribute;
}
abstract class Store {
    private Variable variable;

}
class Update extends Store {
    private List<String> updatedAttribute;
}
class Insert extends Store {
}
abstract class Exp {

}
class UndefinedJudge extends Exp {
    private boolean isUndefined;
    private Variable variable;
}

class BinaryExp extends Exp{
    Exp left;
    public enum OP{
        AND,
        OR;
    }
    OP op;
    Exp right;
}
class Block {

}
class Statement {

}
class AttributeSetter extends Statement {
    private Variable variable;
    private String attribute;
    private Value value;
}
class IfBlock extends Statement {
    private Exp condition;
    private List<Statement> ifTrue;
    private List<Statement> elseTrue;
}
abstract class Value{
}

class VariableValue extends Value {
    // 这个variable的类型一定是basicType
    private Variable basicVariable;
}
class NowTime extends Value {
}
class AttributeValue extends Value {
    private Variable entityVariable;
    private String attribute;
}


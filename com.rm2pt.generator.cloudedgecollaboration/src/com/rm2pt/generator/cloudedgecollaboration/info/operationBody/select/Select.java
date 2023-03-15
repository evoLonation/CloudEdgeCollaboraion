package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.select;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value.Value;

import java.util.ArrayList;
import java.util.List;

public class Select {
    private Variable variable;
    private List<String> selectedAttribute = new ArrayList<>();
    private Condition condition;
    private List<Sort> sortList;

    public Select(Variable variable, Condition condition, List<Sort> sortList) {
        this.variable = variable;
        this.condition = condition;
        this.sortList = sortList;
    }

    public Variable getVariable() {
        return variable;
    }

    public List<String> getSelectedAttribute() {
        return selectedAttribute;
    }

    public Condition getCondition() {
        return condition;
    }


    public void addSelectedAttribute(String selectedAttribute) {
        this.selectedAttribute.add(selectedAttribute);
    }


    public static class Condition{

    }
    public static class AtomicCondition extends Condition {
        private String attributeName;
        private Variable variable;
        public enum OP{
            EQUALS,
            BT,
            LT,
            BQ,
            LQ,
        }
        private OP op;
    }
    public static class BinaryCondition extends Condition{
        public enum OP{
            AND,
            OR,
        }
        private Condition left;
        private Condition right;
        private OP op;
    }
    public static class Sort {
        private String attributeName;
        private Rule rule;
        public enum Rule {
            ASCENDING,
            DESCENDING,
        }
    }
    public static class AttributeEquals extends Condition{
        private String attributeName;
        private Value value;

        public AttributeEquals(String attributeName, Value value) {
            this.attributeName = attributeName;
            this.value = value;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public Value getValue() {
            return value;
        }
    }
}

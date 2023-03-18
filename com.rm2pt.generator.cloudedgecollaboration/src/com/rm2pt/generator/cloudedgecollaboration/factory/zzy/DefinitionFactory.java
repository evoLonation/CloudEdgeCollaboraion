package com.rm2pt.generator.cloudedgecollaboration.factory.zzy;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable;
import net.mydreamy.requirementmodel.rEMODEL.*;

public class DefinitionFactory extends OperationBodyContext{
    private Definition definition;
    private IteratorExpDealer iteratorExpDealer = new IteratorExpDealer();
    private CallExpDealer callExpDealer = new CallExpDealer();
    private AssociationDealer associationDealer = new AssociationDealer();

    public DefinitionFactory(Definition definition) {
        this.definition = definition;
    }

    public void build(){
        definition.getVariable().forEach(this::dealVariable);
    }

    private void dealVariable(VariableDeclarationCS variableDeclaration){
        var variable = new Variable(variableDeclaration.getName(), getType(variableDeclaration.getType()), Variable.ScopeType.DEFINITION);
        var initExp = variableDeclaration.getInitExpression();
        if(initExp instanceof LogicFormulaExpCS){
            initExp = (OCLExpressionCS) ((LogicFormulaExpCS) initExp).getAtomicexp().get(0);
            if(initExp instanceof PropertyCallExpCS) {
                var ret = callExpDealer.dealPropertyCall((PropertyCallExpCS) initExp);
                if(ret instanceof CallExpDealer.PCAttribute) {
                    // todo
                }else if(ret instanceof CallExpDealer.PCGlobalVariable) {
                    // todo
                }else if(ret instanceof CallExpDealer.PCAssociation){
                    var ass = ((CallExpDealer.PCAssociation) ret).association;
                    var sourceVar = ((CallExpDealer.PCAssociation) ret).variable;
                    associationDealer.dealAssGet(variable, sourceVar, ass);
                }else{throw new UnsupportedOperationException();}
            }else if(initExp instanceof IteratorExpDealer){
                iteratorExpDealer.dealIteratorExp((IteratorExpCS) initExp, variable);
            }else{throw new UnsupportedOperationException();}
        }else if(initExp instanceof LiteralExpCS){
            // todo
        }
        variableTable.addVariable(variable);
    }

}

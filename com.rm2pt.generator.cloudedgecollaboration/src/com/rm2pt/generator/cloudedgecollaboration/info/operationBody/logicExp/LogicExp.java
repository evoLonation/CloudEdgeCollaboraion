package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.logicExp;


/**
 * 有两个用途
 * 1、用于Precondition类
 * 2、用于集合操作的Condition类，注意此时不能有UndefinedJudge，且存在一个internalVariable
 */
public abstract class LogicExp {
    public abstract boolean mustPrecondition();
}

package com.rm2pt.generator.cloudedgecollaboration.info.operationBody.value;

import com.rm2pt.generator.cloudedgecollaboration.info.data.BasicType;

public class NowTime extends LiteralValue {

    @Override
    public BasicType getType() {
        return new BasicType(BasicType.TypeEnum.TIME);
    }
}

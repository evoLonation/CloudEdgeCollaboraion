package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;

public class GlobalInfoBuilder {
    public GlobalInfo build(){
        return new GlobalInfo(3, 3,
                3, 3, "myproject");
    }
}

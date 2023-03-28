package com.rm2pt.generator.cloudedgecollaboration.factory;

import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;

public class GlobalInfoBuilder {
    public GlobalInfo build(){
        return new GlobalInfo(1, 30080, 3, 4,
                5, 6, "second-kill");
    }
}

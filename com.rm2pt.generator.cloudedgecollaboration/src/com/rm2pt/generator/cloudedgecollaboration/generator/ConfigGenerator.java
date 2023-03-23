package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.ConfigTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;

import java.util.ArrayList;
import java.util.List;

public class ConfigGenerator extends Generator{
    private String projectName;
    private int dbNum;
    private int tableNum;
    private int redisNum;
    private List<String> operationNameList = new ArrayList<>();
    public ConfigGenerator(List<OperationInfo> highPriorityOperationList, GlobalInfo globalInfo) {
        highPriorityOperationList.forEach(operationInfo -> operationNameList.add(operationInfo.getName()));
        redisNum = globalInfo.getRedisShardingNum();
        tableNum = globalInfo.getTableShardingNum();
        dbNum = globalInfo.getDatabaseShardingNum();
        projectName = globalInfo.getProjectName();
    }

    @Override
    public void generate() {
        generateFile("etc/config.yaml", ConfigTemplate.generateEtc(projectName, operationNameList, dbNum, tableNum, redisNum));
        generateFile("config/config.go", ConfigTemplate.generateConfig(operationNameList));
    }
}

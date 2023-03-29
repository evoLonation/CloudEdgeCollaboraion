package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.generator.zzy.RedisTemplate;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo;

import java.util.ArrayList;
import java.util.List;

public class RedisGenerator extends Generator{
    private int redisNumber;
    private List<String> operationNameList = new ArrayList<>();
    public RedisGenerator(List<OperationInfo> highPriorityOperationList, GlobalInfo globalInfo) {
        highPriorityOperationList.forEach(operationInfo -> operationNameList.add(operationInfo.getName()));
        redisNumber = globalInfo.getRedisShardingNum();
    }

    @Override
    public void generate() {
        generateFile("build/redis/cluster.yaml", RedisTemplate.generateCluster(redisNumber));
        generateFile("build/redis/highpriority.yaml", RedisTemplate.generateHighpriority(operationNameList));
        generateFile("build/redis/mutex.yaml", RedisTemplate.generateMutex());
    }
}

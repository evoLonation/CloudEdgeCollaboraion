package com.rm2pt.generator.cloudedgecollaboration.info;

import java.util.Locale;

public class GlobalInfo {
    private int processNum;
    private int outPort;
    private int databaseShardingNum;
    private int tableShardingNum;
    private int redisShardingNum;
    private int databaseReplicationNum; // master + slave
    private String projectName;

    public GlobalInfo(int processNum, int outPort, int databaseShardingNum, int tableShardingNum, int redisShardingNum, int databaseReplicationNum, String projectName) {
        this.processNum = processNum;
        this.outPort = outPort;
        this.databaseShardingNum = databaseShardingNum;
        this.tableShardingNum = tableShardingNum;
        this.redisShardingNum = redisShardingNum;
        this.databaseReplicationNum = databaseReplicationNum;
        this.projectName = projectName.toLowerCase();
    }

    public int getProcessNum() {
        return processNum;
    }

    public int getDatabaseShardingNum() {
        return databaseShardingNum;
    }

    public int getTableShardingNum() {
        return tableShardingNum;
    }

    public int getRedisShardingNum() {
        return redisShardingNum;
    }

    public int getDatabaseReplicationNum() {
        return databaseReplicationNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getOutPort() {
        return outPort;
    }
}

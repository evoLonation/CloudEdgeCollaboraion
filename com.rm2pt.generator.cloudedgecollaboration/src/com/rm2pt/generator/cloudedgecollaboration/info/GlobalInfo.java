package com.rm2pt.generator.cloudedgecollaboration.info;

public class GlobalInfo {
    private int databaseShardingNum;
    private int tableShardingNum;
    private int redisShardingNum;
    private int databaseReplicationNum; // master + slave
    private String projectName;

    public GlobalInfo(int databaseShardingNum, int tableShardingNum, int redisShardingNum, int databaseReplicationNum, String projectName) {
        this.databaseShardingNum = databaseShardingNum;
        this.tableShardingNum = tableShardingNum;
        this.redisShardingNum = redisShardingNum;
        this.databaseReplicationNum = databaseReplicationNum;
        this.projectName = projectName;
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
}

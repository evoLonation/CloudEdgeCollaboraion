package com.rm2pt.generator.cloudedgecollaboration.generator;

import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder;
import com.rm2pt.generator.cloudedgecollaboration.info.GlobalInfo;
import com.rm2pt.generator.cloudedgecollaboration.info.data.EntityInfo;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.EntityTemplate;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.ReplicationTemplate;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.ShardingTemplate;
import com.rm2pt.generator.cloudedgecollaboration.generator.lyh.SingleTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成mysql相关的d8s部署文件
 */
public class MysqlGenerator extends Generator{
    private final List<EntityInfo> entityInfoList;
    private final List<EntityInfo> replicationInfoList;
    private final List<EntityInfo> shardingInfoList;
    private final List<EntityInfo> singleInfoList;
    private final GlobalInfo globalInfo;
    private final String makeDDL;

    public MysqlGenerator(List<EntityInfo> EntityInfoList, GlobalInfo globalInfo){
        this.entityInfoList = EntityInfoList;
        this.globalInfo = globalInfo;
        replicationInfoList = new ArrayList<>();
        shardingInfoList = new ArrayList<>();
        singleInfoList = new ArrayList<>();
        makeDDL = EntityTemplate.makeDDLContext(Keyworder.camelToUnderScore(globalInfo.getProjectName()));
    }
    // todo implement
    public void generate(){
        classifyEntity();
        generateReplication();
        generateSharding();
        generateSingle();
    }

    private void generateSingle(){
        DDLGenerator ddlGenerator = new DDLGenerator(singleInfoList);
        ddlGenerator.generate();
        String DDLText = ddlGenerator.getDDLText();
        String singleText = SingleTemplate.Context(makeDDL, DDLText);
        generateFile("build/mysql/single.yaml", singleText);
        System.out.println("Generated single.yaml");
    }

    private void generateSharding(){
        int tableNum = globalInfo.getTableShardingNum();
        if (tableNum==1){
            DDLGenerator ddlGenerator = new DDLGenerator(shardingInfoList);
            ddlGenerator.generate();
            String DDLText = ddlGenerator.getDDLText();
            String shardingText = ShardingTemplate.Context(makeDDL, DDLText, globalInfo.getDatabaseShardingNum());
            generateFile("build/mysql/sharding.yaml", shardingText);
        } else { //Separate libraries and tables
            List<EntityInfo> separatedEntityList = new ArrayList<>();
            for (int i = 0; i < tableNum; i++){
                separatedEntityList.addAll(infoListWithSerialNum(i, shardingInfoList));
            }
            DDLGenerator ddlGenerator = new DDLGenerator(separatedEntityList);
            ddlGenerator.generate();
            String DDLText = ddlGenerator.getDDLText();
            String shardingText = ShardingTemplate.Context(makeDDL, DDLText, globalInfo.getDatabaseShardingNum());
            generateFile("build/mysql/sharding.yaml", shardingText);
        }
        System.out.println("Generated sharding.yaml");
    }

    private List<EntityInfo> infoListWithSerialNum(int serialNum, List<EntityInfo> originalList){
        List<EntityInfo> newInfoList = new ArrayList<>();
        for (EntityInfo entityInfo : originalList){
            EntityInfo newInfo = entityInfo.clone();
            newInfo.setName(entityInfo.getName() + "_" + serialNum);
            newInfoList.add(newInfo);
        }
        return newInfoList;
    }

    private void generateReplication(){
        DDLGenerator ddlGenerator = new DDLGenerator(replicationInfoList);
        ddlGenerator.generate();
        String DDLText = ddlGenerator.getDDLText();
        String replicationText = ReplicationTemplate.Context(makeDDL, DDLText, globalInfo.getDatabaseReplicationNum());
        generateFile("build/mysql/replication.yaml", replicationText);
        System.out.println("Generated replication.yaml");
    }

    private void classifyEntity(){
        for (EntityInfo entityInfo : entityInfoList){
            switch (entityInfo.getStorageType()){
                case HIGHREAD:
                    replicationInfoList.add(entityInfo); break;
                case HIGHSTORE:
                    shardingInfoList.add(entityInfo); break;
                case DEFAULT:
                    singleInfoList.add(entityInfo); break;
                default:
                    throw new RuntimeException("Unknown StorageType: " + entityInfo.getStorageType());
            }
        }
    }
}

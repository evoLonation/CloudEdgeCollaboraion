package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo
import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo
import java.util.List

class ContextTemplate {
	static def String generateContext(List<ServiceInfo> serviceInfoList, List<OperationInfo> highPriorityOperationList, String project) {
		'''
		package service
		
		import (
			"fmt"
			"«project»/common"
			"«project»/config"
		
			redis "github.com/go-redis/redis/v8"
			"github.com/jmoiron/sqlx"
			"github.com/sony/sonyflake"
		)
		
		type context struct {
			singleDB         *sqlx.DB
			shardingDB       []*sqlx.DB
			masterDB         *sqlx.DB
			readDB           *sqlx.DB
			rdbs             []*redis.Client
			shardingDBNum    int64
			shardingTableNum int64
			redisClusterNum  int64
			flake            *sonyflake.Sonyflake
		}
		
		func (p *context) generateId() int64 {
			id, err := p.flake.NextID()
			if err != nil {
				log.Fatal(err)
			}
			return int64(id)
		}
		
		func (p *context) shardingTableNameInteger(tableName string, id int64) (*sqlx.DB, string) {
			tableId := id % p.shardingTableNum
			dbId := id / p.shardingTableNum % p.shardingDBNum
			return p.shardingDB[dbId], fmt.Sprintf("%s_%d", tableName, tableId)
		}
		func (p *context) shardingTableNameString(tableName string, id string) (*sqlx.DB, string) {
			return p.shardingTableNameInteger(tableName, common.Hash(id))
		}
		
		func newContext(conf *config.ServiceConf) *context {
		
			shardingDB := make([]*sqlx.DB, conf.ShardingDB.DatabaseNumber)
			for i, source := range conf.ShardingDB.DataSources {
				shardingDB[i] = common.NewMysqlDB(source)
			}
			rdbs := make([]*redis.Client, conf.RedisCluster.NodeNumber)
			for i, redisConf := range conf.RedisCluster.Redis {
				rdbs[i] = common.NewRedisClient(&redisConf)
			}
			return &context{
				singleDB:         common.NewMysqlDB(conf.DataSource),
				shardingDB:       shardingDB,
				masterDB:         common.NewMysqlDB(conf.ReplicationDB.MasterSource),
				readDB:           common.NewMysqlDB(conf.ReplicationDB.ReadSource),
				rdbs:             rdbs,
				shardingDBNum:    conf.ShardingDB.DatabaseNumber,
				shardingTableNum: conf.ShardingDB.TableNumber,
				redisClusterNum:  conf.RedisCluster.NodeNumber,
				flake:            sonyflake.NewSonyflake(sonyflake.Settings{}),
			}
		}
		
		
		func NewServices(conf *config.ServiceConf) («ServiceParser.parseContextServiceParameter(serviceInfoList)») {
			context := newContext(conf)
			«FOR serviceInfo : serviceInfoList»
			«ServiceParser.parseServiceName(serviceInfo, false)» = &«ServiceParser.parseServiceName(serviceInfo, true)»{
				context: context,
			}
			«ENDFOR»
			return
		}
		
		func NewPriorityService(serviceConf *config.ServiceConf, highPriConf *config.HighPriorityConf) *HighPriorityService {
			context := newContext(serviceConf)
			return &HighPriorityService{
				context:       context,
				«FOR hpo : highPriorityOperationList»
				«ServiceParser.parseOperationName(hpo, false)»Rdb: common.NewRedisClient(&highPriConf.«ServiceParser.parseOperationName(hpo, true)»),
				«ENDFOR»
			}
		}
		'''
	}
	
}
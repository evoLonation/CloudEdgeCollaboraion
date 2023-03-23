package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.ArrayList
import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder

class ConfigTemplate {
	static def String generateEtc(String project, List<String> hpOperationList, int dbNum, int tableNum, int redisNum){
		var dbNumList = new ArrayList<Integer>();
		for (var i = 0; i < dbNum; i++) {
		    dbNumList.add(i);
		}
		var redisNumList = new ArrayList<Integer>();
		for (var i = 0; i < redisNum; i++) {
		    redisNumList.add(i);
		}
		'''
		httpServer:
		  Port: 8080
		service:
		  datasource: root:2002116yy@tcp(mysql:3306)/«project»?parseTime=true
		  replicationDB:
		    masterSource: root:2002116yy@tcp(mysql-replication-0.mysql-replication:3306)/«project»?parseTime=true
		    readSource: root:2002116yy@tcp(mysql-read:3306)/«project»?parseTime=true
		  shardingDB:
		    databaseNumber: «dbNum»
		    tableNumber: «tableNum»
		    dataSources: 
		    «FOR num : dbNumList»
		    - root:2002116yy@tcp(mysql-sharding-«num».mysql-sharding:3306)/«project»?parseTime=true
		    «ENDFOR»
		  redisCluster:
		    nodeNumber: «redisNum»
		    redis:
		    «FOR num: redisNumList»
		    - addr: redis-cluster-«num».redis-cluster
		      password: 2002116yy
		    «ENDFOR»
		  highPriority:
		  	«FOR operation: hpOperationList»
		  	«Keyworder.firstLowerCase(operation)»:
		  	  addr: redis-highpriority-«operation.toLowerCase()»
		  	  password: 2002116yy
		  	«ENDFOR»
		'''
	}
	static def String generateConfig(List<String> hpOperationList){
		'''
		package config
		
		import (
			"encoding/json"
			"log"
			"os"
		
			"github.com/pkg/errors"
			"gopkg.in/yaml.v2"
		)
		
		type Conf struct {
			HttpServer   ServerConf       `yaml:"httpServer"`
			Service      ServiceConf      `yaml:"service"`
			HighPriority HighPriorityConf `yaml:"highPriority"`
		}
		type HighPriorityConf struct {
			«FOR operation:hpOperationList»
			«Keyworder.firstUpperCase(operation)» RedisConf `yaml:"«Keyworder.firstLowerCase(operation)»"`
			«ENDFOR»
		}
		type ServiceConf struct {
			DataSource    string            `yaml:"dataSource"`
			ReplicationDB ReplicationDBConf `yaml:"replicationDB"`
			ShardingDB    ShardingDBConf    `yaml:"shardingDB"`
			RedisCluster  RedisClusterConf  `yaml:"redisCluster"`
		}
		type ReplicationDBConf struct {
			MasterSource string `yaml:"masterSource"`
			ReadSource   string `yaml:"readSource"`
		}
		type ShardingDBConf struct {
			DatabaseNumber int64    `yaml:"databaseNumber"`
			TableNumber    int64    `yaml:"tableNumber"`
			DataSources    []string `yaml:"dataSources"`
		}
		type RedisClusterConf struct {
			Redis      []RedisConf `yaml:"redis"`
			NodeNumber int64       `yaml:"nodeNumber"`
		}
		type RedisConf struct {
			Addr     string `yaml:"addr"`
			Password string `yaml:"password"`
		}
		type ServerConf struct {
			Port string `yaml:"port"`
		}
		
		var configFile string = "./etc/config.yaml"
		
		func ParseConfig() (config Conf) {
			dirs, err := os.ReadDir("./etc")
			if err != nil {
				log.Fatal(errors.Wrap(err, "read directory error"))
			}
			var dirInfo string
			for _, dir := range dirs {
				dirInfo += dir.Name() + ", "
			}
			log.Printf("files: %s\n", dirInfo)
		
			log.Println("start read config file")
			content, err := os.ReadFile(configFile)
			// log.Print(string(content))
			if err != nil {
				log.Fatal(errors.Wrap(err, "read config file error"))
			}
			if err := yaml.Unmarshal(content, &config); err != nil {
				log.Fatal(errors.Wrap(err, "unmarshal config file error"))
			}
			conf, _ := json.Marshal(&config)
			log.Print(string(conf))
			return
		}
		'''
	}
}
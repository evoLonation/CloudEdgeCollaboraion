package com.rm2pt.generator.cloudedgecollaboration.generator.lyh


class ShardingTemplate{
    static def String Context(String makeDDL, String DDLText, int num){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: mysql-sharding-config
		data:
		  ddl.sql:  |
		    «makeDDL»
		    «DDLText»
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: mysql-sharding
		spec:
		  ports:
		  - port: 3306
		  clusterIP: None
		  selector:
		    app: mysql-sharding
		---
		apiVersion: apps/v1
		kind: StatefulSet
		metadata:
		  name: mysql-sharding
		spec:
		  serviceName: mysql-sharding # important
		  replicas: «num»
		  selector:
		    matchLabels:
		      app: mysql-sharding
		  template:
		    metadata:
		      labels:
		        app: mysql-sharding
		    spec:
		      containers:
		      - name: mysql
		        image: mysql:8.0.32
		        ports:
		          - containerPort: 3306
		        env:
		        - name: MYSQL_ROOT_PASSWORD
		          value: 2002116yy
		        volumeMounts:
		        - name: init
		          mountPath: /docker-entrypoint-initdb.d
		        livenessProbe:
		          exec:
		            command: ["mysqladmin", "ping"]
		          initialDelaySeconds: 30
		          periodSeconds: 10
		          timeoutSeconds: 5
		        readinessProbe:
		          exec:
		            # Check we can execute queries over TCP (skip-networking is off).
		            command: ["mysql", "-h", "127.0.0.1", "-e", "-p2002116yy", "SELECT 1"]
		          initialDelaySeconds: 5
		          periodSeconds: 2
		          timeoutSeconds: 1
		      volumes:
		      - name: init
		        configMap:
		          name: mysql-sharding-config
		'''
	}
		
}
package com.rm2pt.generator.cloudedgecollaboration.generator.lyh


class SingleTemplate{
    static def String Context(String makeDDL, String DDLText){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: mysql-config
		data:
		  ddl.sql:  |
		    «makeDDL»
		    «DDLText»
		---
		apiVersion: apps/v1
		kind: Deployment
		metadata:
		  name: mysql
		spec:
		  selector:  
		    matchLabels:
		      app: mysql
		  replicas: 1 
		  template: 
		    metadata:
		      labels:
		        app: mysql
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
		        - name: scripts
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
		      - name: scripts
		        configMap:
		          name: mysql-config
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: mysql
		spec:
		  ports:
		  - port: 3306
		  selector:
		    app: mysql
		'''
	}
}
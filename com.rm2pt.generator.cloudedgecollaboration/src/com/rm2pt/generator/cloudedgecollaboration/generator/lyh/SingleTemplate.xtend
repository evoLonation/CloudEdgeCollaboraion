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
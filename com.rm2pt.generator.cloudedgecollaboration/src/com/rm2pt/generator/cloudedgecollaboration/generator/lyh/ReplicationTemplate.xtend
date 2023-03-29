package com.rm2pt.generator.cloudedgecollaboration.generator.lyh


class ReplicationTemplate{
    static def String Context(String makeDDL, String DDLText, int num){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: mysql-replication-config
		data:
		  master.cnf: |
		    [mysqld]
		    log-bin
		    default_authentication_plugin=mysql_native_password
		  # slave.cnf: |
		  #   [mysqld]
		  #   super-read-only
		  change-master.sql: |
		    flush tables with read lock;
		    set global read_only = 1;
		    change master to master_host='mysql-replication-0.mysql-replication',master_user='root',master_password='2002116yy',master_connect_retry=10;
		    start slave;
		  ddl.sql:  |
		    «makeDDL»
		    «DDLText»
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: mysql-replication
		spec:
		  ports:
		  - port: 3306
		  clusterIP: None
		  selector:
		    app: mysql-replication
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: mysql-read
		spec:
		  ports:
		  - port: 3306
		  selector:
		    app: mysql-replication
		---
		apiVersion: apps/v1
		kind: StatefulSet
		metadata:
		  name: mysql-replication
		spec:
		  serviceName: mysql-replication # important
		  replicas: «num»
		  selector:
		    matchLabels:
		      app: mysql-replication
		  template:
		    metadata:
		      labels:
		        app: mysql-replication
		    spec:
		      initContainers:
		      - name: init-mysql
		        image: mysql:8.0.32
		        command:
		        - bash
		        - "-c"
		        - |
		          set -ex
		          [[ $HOSTNAME =~ -([0-9]+)$ ]] || exit 1
		          ordinal=${BASH_REMATCH[1]}
		          echo [mysqld] > mnt/conf.d/server-id.cnf
		          echo server-id=$((100 + $ordinal)) >> mnt/conf.d/server-id.cnf
		          touch /mnt/docker-entrypoint-initdb.d/script.sql
		          cat /mnt/config-map/ddl.sql > /mnt/docker-entrypoint-initdb.d/script.sql
		          if [[ $ordinal -eq 0 ]]; then
		            cp /mnt/config-map/master.cnf /mnt/conf.d
		          else
		            cat /mnt/config-map/change-master.sql > /mnt/docker-entrypoint-initdb.d/script.sql
		          fi                                         
		        volumeMounts:
		        - name: conf
		          mountPath: /mnt/conf.d
		        - name: init
		          mountPath: /mnt/docker-entrypoint-initdb.d
		        - name: config-map
		          mountPath: /mnt/config-map
		      containers:
		      - name: mysql
		        image: mysql:8.0.32
		        ports:
		          - containerPort: 3306
		        env:
		        - name: MYSQL_ROOT_PASSWORD
		          value: 2002116yy
		        volumeMounts:
		          - name: conf
		            mountPath: /etc/mysql/conf.d
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
		      - name: conf
		        emptyDir: {}
		      - name: init
		        emptyDir: {}
		      - name: config-map
		        configMap:
		          name: mysql-replication-config
		'''
    }
}
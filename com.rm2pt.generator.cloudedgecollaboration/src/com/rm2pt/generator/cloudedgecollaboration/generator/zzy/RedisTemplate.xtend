package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.List

class RedisTemplate {
	static def String generateHighpriority(List<String> hpOperationList){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: redis-highpriority-config
		data:
		  redis.conf: |
		    port 6379
		    bind 0.0.0.0
		    appendonly no
		    daemonize no
		    requirepass 2002116yy
		---
		«FOR operation: hpOperationList»
		apiVersion: v1
		kind: Service
		metadata:
		  name: redis-highpriority-«operation.toLowerCase()»
		  labels:
		    app: redis-highpriority-«operation.toLowerCase()»
		spec:
		  ports:
		  - port: 6379
		    targetPort: 6379
		    name: client
		  selector:
		    app: redis-highpriority-«operation.toLowerCase()»
		---
		apiVersion: apps/v1
		kind: Deployment
		metadata:
		  name: redis-highpriority-«operation.toLowerCase()»
		spec:
		  replicas: 1
		  selector:
		    matchLabels:
		      app: redis-highpriority-«operation.toLowerCase()»
		  template:
		    metadata:
		      labels:
		        app: redis-highpriority-«operation.toLowerCase()»
		    spec:
		      containers:
		      - name: redis-highpriority-«operation.toLowerCase()»
		        image: redis:6.2.6
		        ports:
		        - containerPort: 6379
		          name: client
		        command:
		        - bash
		        - "-c"
		        - redis-server /conf/redis.conf 
		        readinessProbe: # 用于监测pod是否准备好
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 5
		          timeoutSeconds: 5
		        livenessProbe: # 用于周期监测pod是否正常
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 20
		          periodSeconds: 3
		        volumeMounts:
		        - name: conf
		          mountPath: /conf
		          readOnly: false
		      volumes:
		      - name: conf
		        configMap:
		          name: redis-cluster-config
		«ENDFOR»

		'''
	}
	static def String generateCluster(int number){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: redis-cluster-config
		data:
		  redis.conf: |
		    port 6379
		    bind 0.0.0.0
		    appendonly no
		    daemonize no
		    requirepass 2002116yy
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: redis-cluster
		  labels:
		    app: redis-cluster
		spec:
		  ports:
		  - port: 6379
		    targetPort: 6379
		    name: client
		  clusterIP: None
		  selector:
		    app: redis-cluster
		---
		apiVersion: apps/v1
		kind: StatefulSet
		metadata:
		  name: redis-cluster
		spec:
		  serviceName: redis-cluster
		  replicas: «number»
		  selector:
		    matchLabels:
		      app: redis-cluster
		  template:
		    metadata:
		      labels:
		        app: redis-cluster
		    spec:
		      containers:
		      - name: redis-cluster
		        image: redis:6.2.6
		        ports:
		        - containerPort: 6379
		          name: client
		        command:
		        - bash
		        - "-c"
		        - redis-server /conf/redis.conf 
		        readinessProbe: # 用于监测pod是否准备好
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 5
		          timeoutSeconds: 5
		        livenessProbe: # 用于周期监测pod是否正常
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 20
		          periodSeconds: 3
		        volumeMounts:
		        - name: conf
		          mountPath: /conf
		          readOnly: false
		      volumes:
		      - name: conf
		        configMap:
		          name: redis-cluster-config
		'''
	}
	static def String generateMutex(){
		'''
		apiVersion: v1
		kind: ConfigMap
		metadata:
		  name: redis-mutex-config
		data:
		  redis.conf: |
		    port 6379
		    bind 0.0.0.0
		    appendonly no
		    daemonize no
		    requirepass 2002116yy
		---
		apiVersion: v1
		kind: Service
		metadata:
		  name: redis-mutex
		  labels:
		    app: redis-mutex
		spec:
		  ports:
		  - port: 6379
		    targetPort: 6379
		    name: client
		  selector:
		    app: redis-mutex
		---
		apiVersion: apps/v1
		kind: Deployment
		metadata:
		  name: redis-mutex
		spec:
		  replicas: 1
		  selector:
		    matchLabels:
		      app: redis-mutex
		  template:
		    metadata:
		      labels:
		        app: redis-mutex
		    spec:
		      containers:
		      - name: redis-mutex
		        image: redis:6.2.6
		        ports:
		        - containerPort: 6379
		          name: client
		        command:
		        - bash
		        - "-c"
		        - redis-server /conf/redis.conf 
		        readinessProbe: # 用于监测pod是否准备好
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 5
		          timeoutSeconds: 5
		        livenessProbe: # 用于周期监测pod是否正常
		          exec:
		            command:
		            - sh
		            - -c
		            - "redis-cli -h $(hostname) ping"
		          initialDelaySeconds: 20
		          periodSeconds: 3
		        volumeMounts:
		        - name: conf
		          mountPath: /conf
		          readOnly: false
		      volumes:
		      - name: conf
		        configMap:
		          name: redis-mutex-config
		'''
	}
}
package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder

class CommonPackageTemplate {
	static def String generateDb(){
		'''
		package common
		
		import (
			"log"
		
			_ "github.com/go-sql-driver/mysql"
			"github.com/jmoiron/sqlx"
			"github.com/pkg/errors"
		)
		
		func NewMysqlDB(dataSource string) *sqlx.DB {
			db, err := sqlx.Open("mysql", dataSource)
			if err != nil {
				log.Fatal(errors.Wrap(err, "open db error"))
			}
			return db
		}
		'''
	}
	static def String generateHttp(){
		'''
		package common
		
		import (
			"net/http"
		
			"github.com/gin-gonic/gin"
		)
		
		// 该函数返回一个gin.H，gin.H是一个map，存储着键值对，将要返回给请求者
		func errorResponse(err error) gin.H {
			return gin.H{"error": err.Error()}
		}
		
		func GetGinHandler[REQ any, RES any](logic func(req *REQ) (*RES, error)) func(ctx *gin.Context) {
			return func(ctx *gin.Context) {
				var req REQ
				if err := ctx.ShouldBindJSON(&req); err != nil {
					//证明请求对于该结构体并不有效
					ctx.JSON(http.StatusBadRequest, errorResponse(err))
					return
				}
				res, err := logic(&req)
				if err != nil {
					ctx.JSON(http.StatusPreconditionFailed, errorResponse(err))
				} else{
					ctx.JSON(http.StatusOK, res)
				}
			}
		}
		'''
	}
	static def String generateRedis(String project){
		'''
		package common
		
		import (
			"«project»/config"
			"log"
		
			redis "github.com/go-redis/redis/v8"
		)
		
		func NewRedisClient(config *config.RedisConf) *redis.Client {
		
			rdb := redis.NewClient(&redis.Options{
				Addr:     config.Addr,
				Password: config.Password, // 没有密码，默认值
				DB:       0,               // 默认DB 0
			})
			log.Println("get rdb success")
			return rdb
		}
		'''
	}
	static def String generateTool(){
		'''
		package common
		
		import (
			"hash/fnv"
			"sync"
		)
		
		func Block() {
			wg := sync.WaitGroup{}
			wg.Add(1)
			wg.Wait()
		}
		func Hash(key string) int64 {
			h := fnv.New64()
			h.Write([]byte(key))
			return int64(h.Sum64())
		}
		'''
	}
	static def String generateGoMod(String project){
		'''
		module «project»
		
		go 1.19
		
		require (
			github.com/adjust/rmq/v5 v5.1.1
			github.com/gin-gonic/gin v1.9.0
			github.com/go-redis/redis/v8 v8.11.5
			github.com/go-sql-driver/mysql v1.7.0
			github.com/jmoiron/sqlx v1.3.5
			github.com/pkg/errors v0.9.1
			gopkg.in/yaml.v2 v2.4.0
			github.com/sony/sonyflake v1.1.0
		)
		'''
	}
	static def String generateHighCommon(String project){
		'''
		package highpriority
		
		import (
			"«project»/common"
			"«project»/config"
			"log"
		
			"github.com/adjust/rmq/v5"
			"github.com/go-redis/redis/v8"
			"github.com/pkg/errors"
		)
		
		type req[REQ any] struct {
			Topic string `json:"topic"`
			Param *REQ   `json:"param"`
		}
		type res[RES any] struct {
			Err    string `json:"err"`
			Result *RES   `json:"result"`
		}
		
		type commonContext struct {
			queue rmq.Queue
			rdb   *redis.Client
		}
		
		func newContext(config *config.RedisConf) *commonContext {
			rdb := common.NewRedisClient(config)
			connection, err := rmq.OpenConnectionWithRedisClient("consumer", rdb, nil)
			if err != nil {
				log.Fatal(errors.Wrap(err, "open rmq connection error"))
			}
			queue, err := connection.OpenQueue("queue")
			if err != nil {
				log.Fatal(errors.Wrap(err, "open rmq queue error"))
			}
			return &commonContext{
				queue: queue,
				rdb:   rdb,
			}
		}
		'''
	}
	static def String generateConsumer(String project){
		'''
		package highpriority
		
		import (
			"context"
			"encoding/json"
			"«project»/config"
			"log"
			"sync"
			"time"
		
			"github.com/adjust/rmq/v5"
		)
		
		type ConsumerRoutine[REQ any, RES any] struct {
			*commonContext
		}
		
		func NewConsumerRoutine[REQ any, RES any](config *config.RedisConf) *ConsumerRoutine[REQ, RES] {
			return &ConsumerRoutine[REQ, RES]{
				commonContext: newContext(config),
			}
		}
		
		func (p *ConsumerRoutine[REQ, RES]) StartComsumer(handler func(req *REQ) (*RES, error)) {
			if err := p.queue.StartConsuming(1000, 100*time.Millisecond); err != nil {
				log.Fatal(err)
			}
			wg := sync.WaitGroup{}
			wg.Add(1)
			p.queue.AddConsumerFunc("handler", func(delivery rmq.Delivery) {
				wg.Wait()
				req := &req[REQ]{}
				err := json.Unmarshal([]byte(delivery.Payload()), req)
				if err != nil {
					log.Fatal(err)
				}
				topic := req.Topic
				param := req.Param
				res := &res[RES]{}
				res.Result, err = handler(param)
				res.Err = err.Error()
				if err := p.rdb.Publish(context.Background(), topic, "done").Err(); err != nil {
					log.Fatal(err)
				}
				wg.Add(1)
			})
		}
		
		'''
	}
	static def String generateProducer(String project){
		'''
		package highpriority
		
		import (
			"context"
			"encoding/json"
			"fmt"
			"«project»/config"
			"log"
			"strconv"
			"time"
		
			"github.com/pkg/errors"
		)
		
		type Producer[REQ any, RES any] struct {
			*commonContext
		}
		
		func NewProducer[REQ any, RES any](config *config.RedisConf) *Producer[REQ, RES] {
			return &Producer[REQ, RES]{
				commonContext: newContext(config),
			}
		}
		
		func (p *Producer[REQ, RES]) Publish(paramStruct *REQ) (*RES, error) {
			topic := strconv.Itoa(int(time.Now().Unix()))
			ctx := context.Background()
			sub := p.rdb.Subscribe(ctx, topic)
			req := &req[REQ]{
				Topic: topic,
				Param: paramStruct,
			}
			payload, err := json.Marshal(req)
			if err != nil {
				log.Fatal(errors.Wrap(err, "json marshal error"))
			}
			if err := p.queue.Publish(string(payload)); err != nil {
				log.Printf("failed to publish: %s", err)
			}
			msg, err := sub.ReceiveMessage(ctx)
			if err != nil {
				log.Fatal(err)
			}
			res := &res[RES]{}
			if err := json.Unmarshal([]byte(msg.Payload), res); err != nil {
				log.Fatal(err)
			}
			fmt.Println("Received message from " + msg.Channel + " channel.")
			sub.Unsubscribe(ctx)
			return res.Result, errors.New(res.Err)
		}
		
		'''
	}
	static def String generateMainDeploy(String project, int processNumber, int serverPort){
		'''
		apiVersion: v1
		kind: Service
		metadata:
		  name: «project»
		spec:
		  type: NodePort
		  ports:
		  - port: 8080
		    nodePort: «serverPort»
		  selector:
		    app: «project»
		---
		apiVersion: apps/v1
		kind: Deployment
		metadata:
		  name: «project»
		spec:
		  selector:
		    matchLabels:
		      app: «project»
		  replicas: «processNumber»
		  template:
		    metadata:
		      labels:
		        app: «project»
		    spec:
		      containers:
		      - name: «project»
		        image: evolonation/«project»:v1.0
		        imagePullPolicy: IfNotPresent # 如果image为本地构建则需要该参数
		        ports:
		        - containerPort: 8080
		'''
	}
	static def String generateDeploy(String project){
		'''
		#!/bin/bash
		. undeploy.sh
		
		docker build -f Dockerfile .. -t evolonation/«project»:v1.0 --push
		kubectl create -f main.yaml 
		kubectl create -f mysql
		kubectl create -f redis
		'''
	}
	static def String generateUndeploy(String project){
		'''
		#!/bin/bash
		docker image rm evolonation/«project»:v1.0
		kubectl delete -f mysql
		kubectl delete -f redis
		kubectl delete -f main.yaml 
		
		'''
	}
	static def String generateDockerfile(){
		'''
		FROM golang:alpine AS builder
		
		LABEL stage=gobuilder
		
		ENV CGO_ENABLED 0 
		ENV GOPROXY https://goproxy.cn,direct
		ENV GOCACHE /build/.cache/go-build
		RUN go install golang.org/x/tools/cmd/goimports@latest
		
		WORKDIR /build
		
		COPY go.mod .
		COPY ./config ./config
		COPY ./main ./main
		COPY ./highpriority ./highpriority
		COPY ./server ./server
		COPY ./service ./service
		COPY ./entity ./entity
		COPY ./common ./common
		RUN go mod tidy
		RUN goimports -w .
		RUN --mount=type=cache,target=/build/.cache/go-build go build -ldflags="-s -w" -o /app/main ./main/server/main.go
		
		FROM scratch
		
		WORKDIR /app
		COPY --from=builder /app/main ./
		COPY ./etc ./etc
		
		CMD ["./main"]
		

		
		'''
	}
	static def String generateMain(String project, List<String> serviceList){
		'''
		package main
		
		import (
			"«project»/common"
			"«project»/config"
			"«project»/server"
			"«project»/service"
		)
		
		func main() {
			conf := config.ParseConfig()
			«FOR service : serviceList SEPARATOR ', '»«Keyworder.firstLowerCase(service)»«ENDFOR» := service.NewServices(&conf.Service)
			server.Start(&conf.HttpServer, &conf.HighPriority, «FOR service : serviceList SEPARATOR ', '»«Keyworder.firstLowerCase(service)»«ENDFOR», service.NewPriorityService(&conf.Service, &conf.HighPriority))
			common.Block()
		}
		
		'''
	}
}
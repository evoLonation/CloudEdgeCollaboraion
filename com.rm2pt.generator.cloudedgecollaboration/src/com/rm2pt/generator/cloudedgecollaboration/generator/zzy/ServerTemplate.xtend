package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.List
import java.util.ArrayList
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder

class ServerTemplate {
	static class Param{
		String name;
		String type;
		
		new(String string, String string2) {
			name = string;
			type = string2;
		}
		
	}
	static def String generateReq(String operation, List<String> nameList, List<String> typeList){
		var paramList = new ArrayList<Param>();
		for(var i = 0; i < nameList.size(); i ++){
			paramList.add(new Param(nameList.get(i), typeList.get(i)));
		}
		'''
		type «Keyworder.firstLowerCase(operation)»Req struct {
			«FOR param : paramList»
			«Keyworder.firstUpperCase(param.name)» «param.type» `json:"«Keyworder.camelToUnderScore(param.name)»"`
			«ENDFOR»
		}
		'''
	}
	static def String generateRes(String operation, String type){
		'''
		type «Keyworder.firstLowerCase(operation)»Res struct {
			Result «type» `json:"result"`
		}
		'''
	}
	static def String generateStartFuncFragment(String serviceName, String operationName, List<String> paramList){
		'''
		router.POST("«Keyworder.camelToDivider(serviceName)»/«Keyworder.camelToDivider(operationName)»", common.GetGinHandler(func(req *«Keyworder.firstLowerCase(operationName)»Req) (*«Keyworder.firstLowerCase(operationName)»Res, error) {
			ret, err := «Keyworder.firstLowerCase(serviceName)».«Keyworder.firstUpperCase(operationName)»(«FOR param : paramList SEPARATOR ', '»req.«Keyworder.firstUpperCase(param)»«ENDFOR»)
			return &«Keyworder.firstLowerCase(operationName)»Res{Result: ret}, err
		}))
		'''
	}
	static def String generateStartFuncFragmentHP(String serviceName, String operationName, List<String> paramList){
		var req = '''«Keyworder.firstLowerCase(operationName)»Req'''
		var res = '''«Keyworder.firstLowerCase(operationName)»Res'''
		'''
		producer := highpriority.NewProducer[«req», «res»](&highConf.«Keyworder.firstUpperCase(operationName)»)
		router.POST("«Keyworder.camelToDivider(serviceName)»/«Keyworder.camelToDivider(operationName)»/high-priority", common.GetGinHandler(func(req *«req») (*«res», error) {
			return producer.Publish(req)
		}))
		'''
	}
	static def String generateStartFunc(List<String> serviceList, List<String> fragmentList){
		'''
		func Start(serverConf *config.ServerConf, highConf *config.HighPriorityConf, «FOR service: serviceList»«Keyworder.firstLowerCase(service)» *service.«Keyworder.firstUpperCase(service)», «ENDFOR»highPriorityService *service.HighPriorityService) {
			router := gin.Default()
			«FOR fragment : fragmentList»
			«fragment»
			«ENDFOR»
			go func() {
				router.Run(":" + serverConf.Port)
			}()
		}
		'''
	}
	static def String generateServer(String projectName, List<String> reqList, List<String> resList, String start){
		var package = '''«projectName»'''
		'''
		package server
		
		import (
			"«package»/common"
			"«package»/config"
			"«package»/highpriority"
			"«package»/service"
		
			"github.com/gin-gonic/gin"
		)
		«FOR req : reqList»
		«req»
		«ENDFOR»
		«FOR res : resList»
		«res»
		«ENDFOR»
		«start»
		'''
	}
	
}
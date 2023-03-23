package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.OperationInfo
import java.util.List

class HighPriorityTemplate {
	static def String generateHighPriority(List<OperationInfo> highPriorityOperationList) {
		'''
		package service
		
		import "github.com/go-redis/redis/v8"
		
		type HighPriorityService struct {
			*context
			enterItemsRdb *redis.Client
		}
		
		«FOR hpo : highPriorityOperationList»
		func (p *HighPriorityService) «ServiceParser.parseOperationName(hpo, true)»(«ServiceParser.parseOperationParameter(hpo)») («ServiceParser.parseOperationReturn(hpo)», err error) {
			«ServiceParser.getHPOBodyString(hpo)»
		}
		«ENDFOR»
		'''
	}
}
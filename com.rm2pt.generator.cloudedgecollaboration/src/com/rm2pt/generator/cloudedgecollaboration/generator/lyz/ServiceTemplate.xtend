package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo

class ServiceTemplate {
	static def String generateService(ServiceInfo service, String project) {
		'''
		package service
		
		import (
			ctx "context"
			"database/sql"
			"encoding/json"
			"fmt"
			"log"
			"«project»/entity"
			"sync"
			"time"
		
			redis "github.com/go-redis/redis/v8"
			"github.com/pkg/errors"
		)
		
		
		type «service.getName()» struct {
			*context
		}
		
		«FOR operation : service.getOperationList()»
		func (p *«service.getName()») «ServiceParser.parseOperationName(operation, true)»(«ServiceParser.parseOperationParameter(operation)») («ServiceParser.parseOperationReturn(operation)», err error) {
			«ServiceParser.getOperationBodyString(operation)»
		}
		«ENDFOR»
		
		'''
	}
}
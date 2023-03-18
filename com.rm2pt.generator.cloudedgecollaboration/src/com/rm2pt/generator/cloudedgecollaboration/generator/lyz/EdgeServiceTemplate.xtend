package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable
import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.ServiceParser
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.GolangOperation

class EdgeServiceTemplate {
	static def String generateService(ServiceInfo service, List<Variable> globalVariableList, List<GolangOperation> operationList) {
		'''
		package service

		import (
			"database/sql"
			"fmt"
			"log"
			"patient-edge/cloud/rpc/cloudclient"
			"patient-edge/entity"
			"sync"
			"time"
			mqtt "github.com/eclipse/paho.mqtt.golang"
			"github.com/pkg/errors"
		)

		type «service.getName()» struct {
			*context
			«FOR variable : globalVariableList»
			«variable.getName()» «ServiceParser.parseType(variable.getType())»
			«ENDFOR»
		}

		«FOR operation : operationList»
		func (p *«service.getName()») «operation.getName()»(«ServiceParser.parseOperationParameter(operation)») error {
			«IF operation.isPublic()»
			«FOR op : ServiceParser.parseOperationErr(service, operationList, operation)»
			if err := p.«op.getName()»(«ServiceParser.parseOperationParameterWithoutType(op)»); err != nil {
				return errors.Wrap(err, "«op.getName()» error")
			}
			«ENDFOR»
			«ELSE»
			log.Println("operation «operation.getName()» start")
			«ENDIF»
			
			«FOR stmt : operation.getOperationBody().getStatementList()»
				«stmt.toString()»
			«ENDFOR»
		}

		«ENDFOR»
		'''

	}
}
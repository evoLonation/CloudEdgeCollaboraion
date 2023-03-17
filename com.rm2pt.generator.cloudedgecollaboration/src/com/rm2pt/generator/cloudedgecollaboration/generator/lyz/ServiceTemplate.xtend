package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable
import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.info.Operation
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.ServiceParser

class ServiceTemplate {
	static def String generateService(ServiceInfo service) {
		ServiceParser parser = new ServiceParser(service);
		List<Variable> globalVariableList = service.getGlobalVariableList();
		List<Operation> operationList = service.getOperationList();
		List<Variable> inputParamList = service.getInputParamList();
		'''
		package service
		
		import (
			"patient-edge/common"
			"patient-edge/entity"
			"github.com/pkg/errors"
		)
		
		type «service.getName()» struct {
			*context
			«FOR variable : globalVariableList»
			«variable.getName()» «ServiceParser.parseType(variable.getType())»
			«ENDFOR»
		}
		
		«FOR operation : operationList»
		func (p *«service.getName()») «operation.getName()»(«paser.parseOperationParameter(operation.getName())») error {
			«// TODO: operationBody»
			«operation.getOperationBody().toString()»
		}

		«ENDFOR»
		'''
	}
}
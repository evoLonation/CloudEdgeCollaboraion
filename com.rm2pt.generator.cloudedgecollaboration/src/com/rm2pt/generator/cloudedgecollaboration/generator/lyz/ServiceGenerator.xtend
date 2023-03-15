package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

class ServiceGenerator {
	def service(ServiceInfo service) {
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
		
		type «service.name» struct {
			*context
			«FOR variable : globalVariableList»
			«variable.name» «variable.type.name»
			«ENDFOR»
		}
		
		«FOR operation : operationList»
		func (p *«service.name») «operation.name» («FOR input : inputParamList» «input.name» «input.type.name», «ENDFOR») error {
			
		}
		«ENDFOR»
		'''
	}
}
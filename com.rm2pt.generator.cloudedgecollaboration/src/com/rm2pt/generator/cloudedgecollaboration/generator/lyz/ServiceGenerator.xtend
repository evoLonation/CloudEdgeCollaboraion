package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable
import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.info.Operation
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo

class ServiceGenerator {
	def service(ServiceInfo service) {
//		List<Variable> globalVariableList = service.getGlobalVariableList();
//		List<Operation> operationList = service.getOperationList();
//		List<Variable> inputParamList = service.getInputParamList();
//		'''
//		package service
//		
//		import (
//			"patient-edge/common"
//			"patient-edge/entity"
//			"github.com/pkg/errors"
//		)
//		
//		type «service.name» struct {
//			*context
//			«FOR variable : globalVariableList»
//			«variable.name» «variable.type.name»
//			«ENDFOR»
//		}
//		
//		«FOR operation : operationList»
//		func (p *«service.name») «operation.name» («FOR input : inputParamList» «input.name» «input.type.name», «ENDFOR») error {
//			
//		}
//		«ENDFOR»
//		'''
	}
}
package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.data.Variable
import java.util.List
import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.ServiceParser
import com.rm2pt.generator.cloudedgecollaboration.generator.lyz.GolangOperation

class CloudServiceTemplate {
	static def String generateService(ServiceInfo service, List<Variable> globalVariableList, List<GolangOperation> operationInfoList) {
		'''
«««		package service
«««
«««		import (
«««			"patient-edge/common"
«««			"patient-edge/entity"
«««			"github.com/pkg/errors"
«««		)
«««
«««		type «service.getName()» struct {
«««			*context
«««			«FOR variable : globalVariableList»
«««			«variable.getName()» «ServiceParser.parseType(variable.getType())»
«««			«ENDFOR»
«««		}
«««
«««		«FOR operationInfo : operationInfoList»
«««		func (p *«service.getName()») «operationInfo.getName()»(«ServiceParser.parseOperationParameter(operationInfo)») error {
«««			«IF operationInfo.isPublic()»
«««			«FOR op : ServiceParser.parseOperationErr(service, operationInfoList, operationInfo)»
«««			if err := p.«op.getName()»(«ServiceParser.parseOperationParameterWithoutType(op)»); err != nil {
«««				return errors.Wrap(err, "«op.getName()» error")
«««			}
«««			«ENDFOR»
«««			«ENDIF»
«««			
«««			«FOR stmt : operationInfo.getOperationBody().getStatementList()»
«««				«stmt.toString()»
«««			«ENDFOR»
«««		}
«««
«««		«ENDFOR»
		'''

	}
}
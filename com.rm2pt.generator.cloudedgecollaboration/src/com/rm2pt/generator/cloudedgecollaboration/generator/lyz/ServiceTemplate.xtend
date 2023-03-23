package com.rm2pt.generator.cloudedgecollaboration.generator.lyz;

import com.rm2pt.generator.cloudedgecollaboration.info.ServiceInfo

class ServiceTemplate {
	static def String generateService(ServiceInfo service) {
		'''
		package service
		
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
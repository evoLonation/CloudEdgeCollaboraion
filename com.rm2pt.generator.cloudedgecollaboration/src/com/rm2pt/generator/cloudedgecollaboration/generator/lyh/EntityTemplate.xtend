package com.rm2pt.generator.cloudedgecollaboration.generator.lyh

import java.util.List

class EntityTemplate{
    static def String SQLContext(List<EntityStr> entityList){
    	'''
«FOR entity : entityList»
CREATE TABLE «entity.getName()» (
«entity.getPrimaryKey().getName()» «entity.getPrimaryKey().getType()»,
«FOR attribute : entity.getAttributes()»
	«attribute.getName()» «attribute.getType()»,
«ENDFOR»
PRIMARY KEY («entity.getPrimaryKey().getName()»)
);

«ENDFOR»
		'''
    }
    
    static def String GOContext(List<EntityStr> entityList){
    	'''
    		package entity
    		import "time"
    		
    		«FOR entity : entityList»
    		type «entity.getName()» struct {
    			«entity.getPrimaryKey().getName()» «entity.getPrimaryKey().getType()» `db:"«entity.getPrimaryKey().getSQLName()»"`
    			«FOR attribute : entity.getAttributes()»
    			«attribute.getName()» «attribute.getType()» `db:"«attribute.getSQLName()»"`
    			«ENDFOR»
    		}
    		
    		
    		«ENDFOR»
    	'''
    }
}

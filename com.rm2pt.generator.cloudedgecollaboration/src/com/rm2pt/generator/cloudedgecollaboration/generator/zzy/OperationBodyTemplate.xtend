package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.List
import java.util.Set
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder

class OperationBodyTemplate {
	static def String generate(List<String> variableList, List<String> selectList, String preconditionExp, String postcondition){
		'''
		// variable declare
		«FOR variable : variableList»
		«variable»
		«ENDFOR»
		
		// select statements
		wg := sync.WaitGroup{}
		«FOR select:selectList»
		wg.Add(1)
		go func() {
			defer wg.Done()
			«select»
		}()
		«ENDFOR»
		wg.Wait()
		
		// precondition
		if !(«preconditionExp») {
			err = errors.New("precondition unsatisfied")
			return
		}
		// postcondition
		«postcondition»
		return 
		'''
	}
	static def String generateSingleSelect(boolean isMulti, String variable, String table, List<String> attrList, String logicExp, List<String> paramList){
		'''
		if err := p.singleDB.«IF isMulti»Select«ELSE»Get«ENDIF»(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `«Keyworder.camelToUnderScore(table)»` where «logicExp»", «FOR param: paramList»«param», «ENDFOR»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateReplicationSelect(boolean isMulti, boolean isConsistency, String variable, String table, List<String> attrList, String logicExp, List<String> paramList){
		'''
		if err := p.«IF isConsistency»masterDB«ELSE»readDB«ENDIF».«IF isMulti»Select«ELSE»Get«ENDIF»(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `«Keyworder.camelToUnderScore(table)»` where «logicExp»", «FOR param: paramList»«param», «ENDFOR»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateShardingSelect(boolean isString, String variable, String idVariable, String table, List<String> attrList, String idAttr){
		'''
		db , table := p.shardingTableName«IF isString»String«ELSE»Integer«ENDIF»("«Keyworder.camelToUnderScore(table)»", «idVariable»)		
		if err := db.Get(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `" + table + "` where «Keyworder.camelToUnderScore(idAttr)» = ?", «idVariable»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateSingleInsert(String variable, String table, List<String>attrList){
		'''
		if _, err := p.singleDB.NamedExec("insert into `«Keyworder.camelToUnderScore(table)»` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	static def String generateReplicationInsert(String variable, String table, List<String>attrList){
		'''
		if _, err := p.masterDB.NamedExec("insert into `«Keyworder.camelToUnderScore(table)»` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	static def String generateShardingInsert(boolean isString, String idAttr, String variable, String table, List<String>attrList){
		'''
		db , table := p.shardingTableName«IF isString»String«ELSE»Integer«ENDIF»("«Keyworder.camelToUnderScore(table)»", «variable».«Keyworder.firstUpperCase(idAttr)»)		
		if _, err := db.NamedExec("insert into `" + table + "` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	static def String generateSingleUpdate(String variable, String table, List<String>attrList){
		'''
		if _, err := p.singleDB.NamedExec("update `«Keyworder.camelToUnderScore(table)»` set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	static def String generateReplicationUpdate(String variable, String table, List<String>attrList){
		'''
		if _, err := p.masterDB.NamedExec("update `«Keyworder.camelToUnderScore(table)»` set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	static def String generateShardingUpdate(boolean isString, String idAttr, String variable, String table, List<String>attrList){
		'''
		db , table := p.shardingTableName«IF isString»String«ELSE»Integer«ENDIF»("«Keyworder.camelToUnderScore(table)»", «variable».«Keyworder.firstUpperCase(idAttr)»)		
		if _, err := db.NamedExec("update " + table + " set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
			log.Fatal(errors.Wrap(err, "insert data error"))
		}
		'''
	}
	
	static def String generatePostcondition(List<String> assignList, List<String> storeList, String result){
		'''
		«FOR assign : assignList»
		«assign»
		«ENDFOR»
		«FOR store:storeList»
		wg.Add(1)
		go func() {
			defer wg.Done()
			«store»
		}()
		«ENDFOR»
		wg.Wait()
		result = «result»
		'''
	}
	static def String generateIfPostcondition(String logic, List<String> assignList, List<String> storeList, String result){
		'''
		if «logic» {
			«FOR assign : assignList»
			«assign»
			«ENDFOR»
			«FOR store:storeList»
			wg.Add(1)
			go func() {
				defer wg.Done()
				«store»
			}()
			«ENDFOR»
			wg.Wait()
			result = «result»
		}
		'''
	}
	
}
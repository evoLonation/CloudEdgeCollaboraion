package com.rm2pt.generator.cloudedgecollaboration.generator.zzy

import java.util.List
import java.util.Set
import com.rm2pt.generator.cloudedgecollaboration.common.Keyworder

class OperationBodyTemplate {
	static def String generate(List<String> mutexList, List<String> variableList, List<String> selectList, String preconditionExp, String postcondition){
		'''
		// mutex
		«generateMutex(mutexList)»
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
	static def String generateSingleSelect(boolean isMulti, String variable, String table, List<String> attrList, String idAttr, String idVariable){
		'''
		«generateRedisGet(variable, idVariable, table)»
		if err := p.singleDB.«IF isMulti»Select«ELSE»Get«ENDIF»(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `«Keyworder.camelToUnderScore(table)»` where «Keyworder.camelToUnderScore(idAttr)» = ?", «idVariable»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateReplicationSelect(boolean isMulti, boolean isConsistency, String variable, String table, List<String> attrList, String idAttr, String idVariable){
		'''
		«generateRedisGet(variable, idVariable, table)»
		if err := p.«IF isConsistency»masterDB«ELSE»readDB«ENDIF».«IF isMulti»Select«ELSE»Get«ENDIF»(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `«Keyworder.camelToUnderScore(table)»` where «Keyworder.camelToUnderScore(idAttr)» = ?", «idVariable»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateShardingSelect(String variable, String table, List<String> attrList, String idAttr, String idVariable){
		'''
		«generateRedisGet(variable, idVariable, table)»
		db , table := p.shardingTableName("«Keyworder.camelToUnderScore(table)»", «idVariable»)		
		if err := db.Get(«variable», "select «FOR attr : attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR» from `" + table + "` where «Keyworder.camelToUnderScore(idAttr)» = ?", «idVariable»); err != nil {
			if err != sql.ErrNoRows {
				log.Fatal(errors.Wrap(err, "select or get error"))
			}else{
				«variable» = nil
			}
		}
		'''
	}
	static def String generateRedisGet(String variable, String idVariable, String table){
		'''
		«variable»Str, err := p.shardingRedis(«idVariable»).Get(ctx.Background(), "«Keyworder.camelToUnderScore(table)»-"+fmt.Sprint(«idVariable»)).Result()
		if err != nil && err != redis.Nil {
			log.Fatal(err)
		}
		if err == nil {
			if err := json.Unmarshal([]byte(«variable»Str), «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "unmarshal error"))
			}
			log.Println("get from redis")
			return
		}
		'''
	}
	static def String generateSingleInsert(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			if _, err := p.singleDB.NamedExec("insert into `«Keyworder.camelToUnderScore(table)»` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateReplicationInsert(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			if _, err := p.masterDB.NamedExec("insert into `«Keyworder.camelToUnderScore(table)»` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}			
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateShardingInsert(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			db , table := p.shardingTableName("«Keyworder.camelToUnderScore(table)»", «variable».«Keyworder.firstUpperCase(idAttr)»)		
			if _, err := db.NamedExec("insert into `" + table + "` («FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»«ENDFOR») values («FOR attr:attrList SEPARATOR ', '»:«Keyworder.camelToUnderScore(attr)»«ENDFOR»)", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}			
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateSingleUpdate(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			if _, err := p.singleDB.NamedExec("update `«Keyworder.camelToUnderScore(table)»` set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}			
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateReplicationUpdate(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			if _, err := p.masterDB.NamedExec("update `«Keyworder.camelToUnderScore(table)»` set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}	
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateShardingUpdate(String variable, String table, List<String>attrList, String idAttr){
		'''
		wg.Add(1)
		go func(){
			defer wg.Done()
			db , table := p.shardingTableName("«Keyworder.camelToUnderScore(table)»", «variable».«Keyworder.firstUpperCase(idAttr)»)		
			if _, err := db.NamedExec("update " + table + " set «FOR attr:attrList SEPARATOR ', '»«Keyworder.camelToUnderScore(attr)»=:«Keyworder.camelToUnderScore(attr)»«ENDFOR»", «variable»); err != nil {
				log.Fatal(errors.Wrap(err, "insert data error"))
			}
		}()
		«generateRedisSet(variable, idAttr, table)»
		'''
	}
	static def String generateRedisSet(String variable, String idAttr, String table){
		'''
		wg.Add(1)
		go func (){
			defer wg.Done()
			var err error
			«variable»Str, err := json.Marshal(«variable»)
			if err != nil {
				log.Fatal(errors.Wrap(err, "marshal error"))
			}
			if _, err := p.shardingRedis(«variable».«Keyworder.firstUpperCase(idAttr)»).Set(ctx.Background(), "«Keyworder.camelToUnderScore(table)»-"+fmt.Sprint(«variable».«Keyworder.firstUpperCase(idAttr)»), «variable»Str, time.Hour).Result(); err != nil {
				log.Fatal(err)
			}
		}()
		'''
	}
	
	static def String generatePostcondition(List<String> assignList, List<String> storeList, String result){
		'''
		«FOR assign : assignList»
		«assign»
		«ENDFOR»
		«FOR store:storeList»
		«store»
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
			«store»
			«ENDFOR»
			wg.Wait()
			result = «result»
		}
		'''
	}
	
	static def String generateMutex(List<String> mutexList){
		'''
		mutexs := []string{«FOR mutex:mutexList SEPARATOR ','»«mutex»«ENDFOR»}
		p.addMutex(mutexs...)
		defer func() {
			p.removeMutex(mutexs...)
		}()
		'''
	}
	
}
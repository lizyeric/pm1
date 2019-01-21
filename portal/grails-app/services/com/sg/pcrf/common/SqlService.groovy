package com.sg.pcrf.common

import grails.transaction.Transactional
import org.springframework.jdbc.core.JdbcTemplate

@Transactional
class SqlService {
    def dataSource
    def static final String TIME_DAY  = "daily"
    def static final String TIME_WEEK  = "weekly"
    def static final String TIME_MONTH  = "monthly"

/**
 * Execute plain sql with Spring JDBC template
 * @param sql
 * @return
 */
    def getDBExecutionResult(def sql){
        def template = new JdbcTemplate(dataSource)
        def result = template.queryForList(sql);
        return result;
    }


    def updateBySql(def sql){
        def template = new JdbcTemplate(dataSource)
        return template.update(sql);
    }



}

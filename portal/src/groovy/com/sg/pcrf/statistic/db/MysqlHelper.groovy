package com.sg.pcrf.statistic.db

import com.sg.pcrf.statistic.util.Utils
import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

public class MysqlHelper {

//	public static int executeUpdate(Connection connection, String sql) {
//		int resCount = 0;
//		PreparedStatement ps = null;
//		try {
//			ps = connection.prepareStatement(sql);
//			resCount = ps.executeUpdate();
//			connection.commit();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				ps.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return resCount;
//
//	}

    public static List<List<String>> executeQueryMapping(Sql sql, String search) {

        List<List<String>> result = new ArrayList<List<String>>();
        try {
            def rows = sql.rows(search);
            rows.each { row ->
                List<String> list = new ArrayList<String>();
                String policy_id = row.policy_id;
                String policy_name = row.policy_name;
                String policy_action = row.policy_action;
                String policy_condition = row.policy_condition;
                String policy_code = row.policy_code;
                String service_code = row.service_code;
                list.add(policy_id);
                list.add(policy_name);
                list.add(policy_action);
                list.add(policy_condition);
                list.add(policy_code)
                list.add(service_code)
                result.add(list);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int searchCount(Sql sql, String search) {
        // TODO Auto-generated method stub
        def count = 0;
        List<GroovyRowResult> rows =  sql.rows(search)
        rows.each { row ->
            count = row.c
        }
        return count;
    }


}

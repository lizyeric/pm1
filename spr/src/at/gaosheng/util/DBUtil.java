package at.gaosheng.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil
{
    public static Connection conn = null;

    private DBUtil(){

    }

    public static synchronized  Connection getConnection(Properties prop){
    	String driver = prop.getProperty("MYSQL_DRIVER").trim();
    	String url = prop.getProperty("MYSQL_URL").trim();
    	String username = prop.getProperty("MYSQL_USERNAME").trim();
    	String password = prop.getProperty("MYSQL_PASSWORD").trim();
//        if(conn == null ){
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        } catch(Exception e){
            System.out.println("getConnection exception");
            e.printStackTrace();
            Utils.printlnInOut("DBUtil",
					"getConnection exception." + e.getMessage());
        }
//        }
        return conn;
    }

    public static void close(){
        try{
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
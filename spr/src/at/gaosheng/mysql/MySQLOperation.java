package at.gaosheng.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MySQLOperation {
	
	//定义数据库的连接
    private Connection connection;
    //定义sql语句的执行对象
    private PreparedStatement pStatement;
    //定义查询返回的结果集合
    private ResultSet resultset;

    public MySQLOperation(Properties prop)
    {
    	String driver = prop.getProperty("MYSQL_DRIVER").trim();
    	String url = prop.getProperty("MYSQL_URL").trim();
    	String username = prop.getProperty("MYSQL_USERNAME").trim();
    	String password = prop.getProperty("MYSQL_PASSWORD").trim();
        try {
            Class.forName(driver);//注册驱动
            connection = DriverManager.getConnection(url,username,password);//定义连接
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    /**
     * 完成对数据库的增删改操作
     * @param sql语句
     * @param 传入的占位符，List集合
     * @return SQL语句执行成功返回true,否则返回false
     * @throws SQLException
     */
    public boolean addDeleteModify(String sql,List<Object>params) throws SQLException
    {
        int result = -1;//设置为
        pStatement = connection.prepareStatement(sql);  //填充占位符
        int index = 1; //从第一个开始添加
        if(params != null && !params.isEmpty())
        {
            for(int i = 0;i<params.size();i++)
            {
                pStatement.setObject(index++,params.get(i));//填充占位符
            }
        }
        result = pStatement.executeUpdate();//执行成功将返回大于0的数
        return  result>0 ? true : false;
    }
   /**
    * 数据库查询操作，返回单条记录
    * @param sql语句
    * @param 传入的占位符
    * @return 返回Map集合类型，包含查询的结果
    * @throws SQLException
    */
    public Map<String,Object> returnSimpleResult(String sql,List<Object>params) throws SQLException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        int index = 1;//从1开始设置占位符
        pStatement = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()) /*判断参数是否为空*/
        { 
            for(int i = 0;i<params.size();i++) /*循环填充占位符*/
            {
                pStatement.setObject(index++, params.get(i));
            }
        }
        resultset = pStatement.executeQuery();
        /*  将查询结果封装到map集合*/
        ResultSetMetaData metaDate = resultset.getMetaData();//获取resultSet列的信息
        int columnLength = metaDate.getColumnCount();//获得列的长度
        while(resultset.next())
        {
            for(int i = 0;i<columnLength;i++)
            {
                String metaDateKey = metaDate.getColumnName(i+1);//获得列名
                Object resultsetValue = resultset.getObject(metaDateKey);//通过列名获得值
                if(resultsetValue == null)
                {
                    resultsetValue = "";//转成String类型
                }
                map.put(metaDateKey, resultsetValue);//添加到map集合（以上代码是为了将从数据库返回的值转换成map的key和value）
            }
        }
        return map;
    }
    /**
     * 查询数据库，返回多条记录
     * @param sql语句
     * @param 占位符
     * @return list集合，包含查询的结果
     * @throws SQLException 
     */
    public List<Map<String,Object>> returnMultipleResult(String sql,List<Object>params) throws SQLException
    {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //填充占位符
        int index = 1;
        pStatement = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty())
        {
            for(int i = 0;i<params.size();i++)
            {
                pStatement.setObject(index++, params.get(i));
            }
        }
        //执行SQL语句
        resultset = pStatement.executeQuery();
        //封装resultset成map类型
        ResultSetMetaData metaDate = resultset.getMetaData();//获取列信息,交给metaDate
        int columnlength = metaDate.getColumnCount();
        while(resultset.next())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            for(int i = 0;i<columnlength;i++)
            {
                String metaDateKey = metaDate.getColumnName(i+1);//获取列名
                Object resultsetValue = resultset.getObject(metaDateKey);
                if(resultsetValue == null)
                {
                    resultsetValue = "";
                }
                map.put(metaDateKey, resultsetValue);
            }
            list.add(map);
        }
        return list;
    }
    
    /**
     * 注意在finally里面执行以下方法，关闭连接
     */
    public void closeconnection()
    {
        if(resultset != null)
        {
            try {
                resultset.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(pStatement != null)
        {
            try {
                pStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(connection != null)
        {
            try {
                connection.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void close(){
    	if(resultset != null)
        {
            try {
                resultset.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(pStatement != null)
        {
            try {
                pStatement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

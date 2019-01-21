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
	
	//�������ݿ������
    private Connection connection;
    //����sql����ִ�ж���
    private PreparedStatement pStatement;
    //�����ѯ���صĽ������
    private ResultSet resultset;

    public MySQLOperation(Properties prop)
    {
    	String driver = prop.getProperty("MYSQL_DRIVER").trim();
    	String url = prop.getProperty("MYSQL_URL").trim();
    	String username = prop.getProperty("MYSQL_USERNAME").trim();
    	String password = prop.getProperty("MYSQL_PASSWORD").trim();
        try {
            Class.forName(driver);//ע������
            connection = DriverManager.getConnection(url,username,password);//��������
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    /**
     * ��ɶ����ݿ����ɾ�Ĳ���
     * @param sql���
     * @param �����ռλ����List����
     * @return SQL���ִ�гɹ�����true,���򷵻�false
     * @throws SQLException
     */
    public boolean addDeleteModify(String sql,List<Object>params) throws SQLException
    {
        int result = -1;//����Ϊ
        pStatement = connection.prepareStatement(sql);  //���ռλ��
        int index = 1; //�ӵ�һ����ʼ���
        if(params != null && !params.isEmpty())
        {
            for(int i = 0;i<params.size();i++)
            {
                pStatement.setObject(index++,params.get(i));//���ռλ��
            }
        }
        result = pStatement.executeUpdate();//ִ�гɹ������ش���0����
        return  result>0 ? true : false;
    }
   /**
    * ���ݿ��ѯ���������ص�����¼
    * @param sql���
    * @param �����ռλ��
    * @return ����Map�������ͣ�������ѯ�Ľ��
    * @throws SQLException
    */
    public Map<String,Object> returnSimpleResult(String sql,List<Object>params) throws SQLException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        int index = 1;//��1��ʼ����ռλ��
        pStatement = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty()) /*�жϲ����Ƿ�Ϊ��*/
        { 
            for(int i = 0;i<params.size();i++) /*ѭ�����ռλ��*/
            {
                pStatement.setObject(index++, params.get(i));
            }
        }
        resultset = pStatement.executeQuery();
        /*  ����ѯ�����װ��map����*/
        ResultSetMetaData metaDate = resultset.getMetaData();//��ȡresultSet�е���Ϣ
        int columnLength = metaDate.getColumnCount();//����еĳ���
        while(resultset.next())
        {
            for(int i = 0;i<columnLength;i++)
            {
                String metaDateKey = metaDate.getColumnName(i+1);//�������
                Object resultsetValue = resultset.getObject(metaDateKey);//ͨ���������ֵ
                if(resultsetValue == null)
                {
                    resultsetValue = "";//ת��String����
                }
                map.put(metaDateKey, resultsetValue);//��ӵ�map���ϣ����ϴ�����Ϊ�˽������ݿⷵ�ص�ֵת����map��key��value��
            }
        }
        return map;
    }
    /**
     * ��ѯ���ݿ⣬���ض�����¼
     * @param sql���
     * @param ռλ��
     * @return list���ϣ�������ѯ�Ľ��
     * @throws SQLException 
     */
    public List<Map<String,Object>> returnMultipleResult(String sql,List<Object>params) throws SQLException
    {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //���ռλ��
        int index = 1;
        pStatement = connection.prepareStatement(sql);
        if(params != null && !params.isEmpty())
        {
            for(int i = 0;i<params.size();i++)
            {
                pStatement.setObject(index++, params.get(i));
            }
        }
        //ִ��SQL���
        resultset = pStatement.executeQuery();
        //��װresultset��map����
        ResultSetMetaData metaDate = resultset.getMetaData();//��ȡ����Ϣ,����metaDate
        int columnlength = metaDate.getColumnCount();
        while(resultset.next())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            for(int i = 0;i<columnlength;i++)
            {
                String metaDateKey = metaDate.getColumnName(i+1);//��ȡ����
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
     * ע����finally����ִ�����·������ر�����
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

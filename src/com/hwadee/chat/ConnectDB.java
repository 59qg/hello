package com.hwadee.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    
    // ������
    public static String driver = "com.mysql.jdbc.Driver";
    
    // ���ӵ�ַ
    public static String url =
                             "jdbc:mysql://localhost:3306/chatwork?characterEncoding=utf8&useSSL=true";
    
    // �û���
    public static String username = "root";
    
    // ����
    public static String password = "123456";
    
    // ����
    private Connection conn;
    
    public Connection getConn() {
        return conn;
    }
    
    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * Title: ���캯�� Description:��������
     */
    public ConnectDB() {
        try {
            Class.forName(driver);
            
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void connDB() {
        try {
            conn = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void insert(String ip, String port, String message) {
        Statement stmt = null;
        try {
            connDB();
            stmt = getConn().createStatement();
            String sql = "insert into info(ip,port,message)" + "values('"
                         + ip
                         + "','"
                         + port
                         + "','"
                         + message
                         + "')";
            stmt.executeUpdate(sql);
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (null != stmt) {
                try {
                    stmt.close();
                    getConn().close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
    
}

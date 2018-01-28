package servletTut.Servlet;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.HashMap;

/**
 * Created by Sameer on 1/26/2018.
 */
public class DBHelper {
    Connection connection;

    public static ResultSet selectFromTable(String tableName) {
        ResultSet resultSet = null;
        try {
            Statement stmt = makeConnection().createStatement();
            String sql;
            sql = "SELECT * FROM  " + tableName;
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return resultSet;
    }

    public static  ResultSet selectFromTable(String tableName,String condition,int conditionValue,String selectValue){
        ResultSet resultSet = null;
        try {
            Statement stmt = makeConnection().createStatement();
            String sql;
            sql = "SELECT "+selectValue+" FROM  " + tableName+" where "+condition+" = "+conditionValue;
            System.out.println(sql);
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return resultSet;
    }

    static Connection makeConnection() {
        Connection connection = null;
        if (connection != null) {
            return connection;
        } else {
            try {
                Class.forName(Constatnt.JDBC_DRIVER);
                connection = DriverManager.getConnection(Constatnt.DB_URL, Constatnt.USER, Constatnt.PASS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    public static Statement getStatement() throws SQLException {
        Statement stmt = makeConnection().createStatement();
        return stmt;
    }
    public static void inserIntoTable(String tableName, HashMap<String, String> items) throws Exception {

        Statement stmt = makeConnection().createStatement();
        String sql;
        sql = "INSERT INTO  token_table " + tableName;
        ResultSet resultSet = stmt.executeQuery(sql);
    }
}

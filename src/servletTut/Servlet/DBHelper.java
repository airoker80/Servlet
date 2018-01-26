package servletTut.Servlet;

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

    public static void inserIntoTable(String tableName, HashMap<String, String> items) throws Exception {

        Statement stmt = makeConnection().createStatement();
        String sql;
        sql = "INSERT * FROM  " + tableName;
        ResultSet resultSet = stmt.executeQuery(sql);
    }
}

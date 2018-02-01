package servletTut.Servlet;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

public class signupServlet extends HttpServlet {
    private String email, name, lastName, password;
    PrintWriter printWriter;
    private String token;

    public void init() throws ServletException {
        super.init();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        printWriter = resp.getWriter();

        Enumeration enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            String parameterValue = req.getParameter(parameterName);
            System.out.println(parameterName + " : " + parameterValue);
        }
        email = req.getParameter("email");
        password = req.getParameter("password");
        name = req.getParameter("name");
        lastName = req.getParameter("lastName");

        try {
            token= makeSHA1Hash(email+password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = DBHelper.getStatement();
            String sql;
            sql = "INSERT INTO user (active, email, last_name, name, password)" +
                    " VALUES " +
                    "(0,'"
                    + email+"','"+
                    lastName+"','"+
                    name+"','"+
                    password+"')";

            System.out.println(sql);
            statement.execute(sql);

            String sql2;
            sql2 = "SELECT user_id FROM user WHERE email='"+email+"'";
            System.out.println(sql2);
            ResultSet resultSet1 = statement.executeQuery(sql2);
//            String userID = resultSet1.getString("user_id");

            JSONObject jsonObject1 = new JSONObject();
            ResultSetMetaData rsmd = resultSet1.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet1.next()) {

                for (int i = 1; i <= columnCount; i++) {
                    String name = rsmd.getColumnName(i);
                    System.out.println("name : " + name);
                    jsonObject1.put(name, resultSet1.getString(name));
                }
                System.out.println(jsonObject1);
            }



        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException){
                System.out.println("Already have email");
                printWriter.println("Already have email");
            }
            e.printStackTrace();
        }


    }

    public void destroy() {
        super.destroy();
    }

    public String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr;
    }
}

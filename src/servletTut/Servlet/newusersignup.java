package servletTut.Servlet;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import jdk.nashorn.internal.runtime.Context;
import org.json.JSONObject;
import servletTut.Servlet.dao.ContextDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Enumeration;

@WebServlet(value = "/signupNew")
public class newusersignup extends HttpServlet {
    ContextDao dao;
    PrintWriter printWriter;
    JSONObject jsonObject = new JSONObject();
    String email, password, token;

    public void init() throws ServletException {
//        dao = new ContextDao(Context.getContext());
        super.init();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Here");
        Enumeration enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            String parameterValue = req.getParameter(parameterName);
            System.out.println(parameterName + " : " + parameterValue);
        }
        email = req.getParameter("email");
        password = req.getParameter("pwd");
        printWriter = resp.getWriter();

        token = email + password + "%$&*))*(&*()";


        System.out.println( email +
                "password"+password +
                token);
        try {
            Connection connection = DBHelper.makeConnection();
            String sql;
            sql = "INSERT INTO newuser(password, email, token) VALUE (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, token);
            int status = preparedStatement.executeUpdate();
            if (status>0){
                JSONObject tokenJSON = new JSONObject();
                ResultSet resultSet= DBHelper.selectFromTable("*","newuser","email",email);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnCount = rsmd.getColumnCount();
                while (resultSet.next()) {

                    for (int i = 1; i <= columnCount; i++) {
                        String name = rsmd.getColumnName(i);
                        System.out.println("name : " + name);
                        tokenJSON.put(name, resultSet.getString(name));
                    }
                    System.out.println(tokenJSON);
                }
                req.getSession().setAttribute("sess_id",tokenJSON.getString("id"));
                jsonObject.put("token",makeSHA1Hash(tokenJSON.getString("token")));
//                jsonObject.put("token",resultSet.getString("token"));
//                jsonObject.put("id",resultSet.getString("id"));
                jsonObject.put("msgTitle","success");
                printWriter.println(jsonObject);
            }else {
                printWriter.println("Some error occured");
            }
        } catch (Exception e) {
            if (e instanceof MySQLIntegrityConstraintViolationException){
                printWriter.println("Email Already Exists");
            }else {
                e.printStackTrace();
            }
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

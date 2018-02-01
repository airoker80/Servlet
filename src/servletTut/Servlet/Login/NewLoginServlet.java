package servletTut.Servlet.Login;

import org.json.JSONObject;
import servletTut.Servlet.Helper.DBHelper;

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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

@WebServlet(value = "/loginNew")
public class NewLoginServlet extends HttpServlet {
    String email, password,newToken;

    public void init() throws ServletException {
        super.init();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        email = req.getParameter("email");
        password = req.getParameter("password");

        PrintWriter printWriter = resp.getWriter();
        ResultSet resultSet = DBHelper.selectFromTable("*", "newuser", "email", email, "password", password);
        try {
            JSONObject tokenJSON = new JSONObject(),jsonObject = new JSONObject();
            if (resultSet != null) {
                newToken = email+password+randomAlphaNumeric(10);
             String updateQuery;
             updateQuery = "UPDATE newuser SET token='"+newToken+"' WHERE email='"+email+"'";
             int resultInt = DBHelper.getStatement().executeUpdate(updateQuery);
             if (resultInt>0){
                 ResultSet resultSet1 = DBHelper.selectFromTable("*", "newuser", "email", email, "password", password);
                 ResultSetMetaData rsmd = resultSet1.getMetaData();
                 int columnCount = rsmd.getColumnCount();
                 while (resultSet1.next()) {

                     for (int i = 1; i <= columnCount; i++) {
                         String name = rsmd.getColumnName(i);
                         System.out.println("name : " + name);
                         tokenJSON.put(name, resultSet1.getString(name));
                     }
                     System.out.println(tokenJSON);
                     req.getSession().setAttribute("sess_id",tokenJSON.getString("id"));
                     jsonObject.put("token",makeSHA1Hash(tokenJSON.getString("token")));
                     jsonObject.put("msgTitle","success");
                     printWriter.println(jsonObject);
                 }
             }
            } else {
                printWriter.println("Wrong Credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}

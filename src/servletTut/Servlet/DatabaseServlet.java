package servletTut.Servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import servletTut.Servlet.Helper.DBHelper;

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

/**
 * Created by Sameer on 1/26/2018.
 */
public class DatabaseServlet extends HttpServlet {
    String string1="sameer",string2="sameer";
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            ResultSet rs = DBHelper.selectFromTable("user");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()){
                JSONObject jsonObject1 = new JSONObject();
                for (int i=1;i<columnCount;i++){
                    String name = rsmd.getColumnName(i);
                    System.out.println("name : "+name);
                    jsonObject1.put(name,rs.getString(name));
                }
                jsonArray.put(jsonObject1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.println(jsonArray);

        System.out.println(
            String.valueOf(string1.hashCode())+ "\n"+
                    String.valueOf(string2.hashCode())

        );

        try {
            System.out.println(makeSHA1Hash(string1) + "\n"+
                    makeSHA1Hash(string2));
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(

        );
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    public String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }
}

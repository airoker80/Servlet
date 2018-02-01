package servletTut.Servlet.Filter;

import org.json.JSONException;
import org.json.JSONObject;
import servletTut.Servlet.DBHelper;
import sun.rmi.runtime.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Enumeration;

public class FirstTestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("initiate filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        PrintWriter printWriter = servletResponse.getWriter();
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession();
        boolean loggedIn = session != null && session.getAttribute("sess_id") != null;
        System.out.println(httpRequest.getMethod());
        switch (httpRequest.getMethod()) {
            case "POST":
                if (httpRequest.getRequestURI().startsWith("/login") | httpRequest.getRequestURI().startsWith("/signup")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    if (loggedIn){
                        String id = (String) session.getAttribute("sess_id");
                        try {
                            System.out.println("id"+id);
                            JSONObject tokenJSON = new JSONObject();
                            ResultSet resultSet= DBHelper.selectFromTable("*","newuser","id",id);
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
                            if (makeSHA1Hash(tokenJSON.getString("token")).equals(httpRequest.getHeader("token"))){
                                filterChain.doFilter(servletRequest, servletResponse);
                            }else {
                                printWriter.println("User Authentication failed");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        printWriter.println("Please login ");
                    }
//                    Enumeration enumeration = servletRequest.getParameterNames();
//                    while (enumeration.hasMoreElements()) {
//                        String parameterName = (String) enumeration.nextElement();
//                        String parameterValue = servletRequest.getParameter(parameterName);
//                        System.out.println(parameterName + " : " + parameterValue);
//                    }
//                    System.out.println("do fILTER");
//                    System.out.println();
//                    JSONObject jsonObject1 = new JSONObject();
//                    try {
//                        ResultSet rs = DBHelper.selectFromTable("token","token_table", "id", "1");
//                        ResultSetMetaData rsmd = rs.getMetaData();
//                        int columnCount = rsmd.getColumnCount();
//                        while (rs.next()) {
//
//                            for (int i = 1; i <= columnCount; i++) {
//                                String name = rsmd.getColumnName(i);
//                                System.out.println("name : " + name);
//                                jsonObject1.put(name, rs.getString(name));
//                            }
//                            System.out.println(jsonObject1);
//                        }
//
//                        System.out.println("token " + makeSHA1Hash(jsonObject1.getString("token")));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        if (((HttpServletRequest) servletRequest).getHeader("token").equals(makeSHA1Hash(jsonObject1.getString("token"))))
//                            filterChain.doFilter(servletRequest, servletResponse);
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }

                break;
            case "GET":
                filterChain.doFilter(servletRequest, servletResponse);
                break;

        }
//        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        System.out.println("Destroy!!!");
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

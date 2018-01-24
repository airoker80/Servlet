package servletTut.Servlet;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by Sameer on 1/24/2018.
 */
public class GetFormServlet extends HttpServlet {
    String message ="";
    public void init() throws ServletException {
//        message="{abcd:abcde}";
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("text/html");
        resp.setContentType("application/json");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",req.getParameter("name"));
            jsonObject.put("roll",req.getParameter("roll"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Enumeration enumeration = req.getParameterNames();
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(enumeration);
        System.out.println("finish");
    }
    public void destroy() {
        super.destroy();
    }
}

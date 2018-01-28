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
import java.util.List;

/**
 * Created by Sameer on 1/24/2018.
 */
public class PostFormServlet extends HttpServlet {
    JSONObject jsonObject = new JSONObject();
    int i = 1;

    public void init() throws ServletException {
        super.init();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setIntHeader("Refresh", 1); /*refresh after every 1 sec*/
        try {
            if (i >= 10) {
                resp.sendError(407, "Need authentication!!!");
                i = 0;
            }
            jsonObject.put("int ", i);
            i++;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.println(jsonObject);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setIntHeader("Refresh", 1);

        Enumeration enumeration = req.getParameterNames();
        System.out.println("content type" + req.getContentType());
        while (enumeration.hasMoreElements()) {
            String paramName = (String) enumeration.nextElement();
            String paramValue = req.getParameter(paramName);
            try {
                jsonObject.put(paramName, paramValue);
                jsonObject.put("int", i);
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject1 = new JSONObject();
        Enumeration enumeration1 = req.getHeaderNames();
        while (enumeration1.hasMoreElements()) {
            String headerName = (String) enumeration1.nextElement();
            String headerValue = req.getHeader(headerName);
            try {
                jsonObject1.put(headerName, headerValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        out.println(jsonObject);
        out.println("\n" + jsonObject1);


    }

    public void destroy() {
        super.destroy();
    }
}

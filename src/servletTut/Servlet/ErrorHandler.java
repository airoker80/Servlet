package servletTut.Servlet;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Sameer on 1/25/2018.
 */

public class ErrorHandler extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer integer = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) req.getAttribute("javax.servlet.error.servlet_name");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");

        if (servletName.equals(null)) {
            servletName= "Unknown";
        }
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("throwable",String.valueOf(throwable));
            jsonObject.put("statusCode",integer);
            jsonObject.put("servletName",servletName);
            jsonObject.put("requestUri",requestUri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = resp.getWriter();
        out.println(jsonObject);
    }
}

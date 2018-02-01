package servletTut.Servlet;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Sameer on 1/26/2018.
 */

public class SessionServlet extends HttpServlet {
    int vistCount= Integer.parseInt(null);


    public void init() throws ServletException {
        super.init();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession httpSession = req.getSession();

        Date cretionTime = new Date(httpSession.getCreationTime());
        Date lastAccessedTime = new Date(httpSession.getLastAccessedTime());

        JSONObject jsonObject = new JSONObject();
        try {
            if (httpSession.isNew()) {
                jsonObject.put("title", "Welcome New User");
                httpSession.setAttribute("sameer","55555");
            } else {
                jsonObject.put("title", "Welcome Back User");
                vistCount = (Integer)httpSession.getAttribute(String.valueOf(vistCount));
                vistCount++;
            }
            jsonObject.put("visitCount",vistCount);
            jsonObject.put("session_id ",httpSession.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PrintWriter out = resp.getWriter();
        out.println(jsonObject);
    }

    public void destroy() {
        super.destroy();
    }
}

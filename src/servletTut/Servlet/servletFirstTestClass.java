package servletTut.Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sameer on 1/24/2018.
 */
public class servletFirstTestClass extends HttpServlet {
    String msg;

    public void init() throws ServletException {
        msg="HELLO WORLD";
        super.init();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        System.out.println("<h1>"+msg+"</h1>");
    }

    public void destroy() {
        super.destroy();
    }
}

package tmall.servlet;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Boli Tao
 */
@WebServlet(name = "TestLog4jServlet")
public class TestLog4jServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(TestLog4jServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("This is a logging statement from log4j");
        String html = "<html><h2>Log4j has been initialized successfully!</h2></html>";
        resp.getWriter().println(html);
        logger.debug("Test");
    }
}

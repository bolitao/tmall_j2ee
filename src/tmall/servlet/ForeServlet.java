package tmall.servlet;

import org.apache.log4j.Logger;
import tmall.bean.Category;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Boli Tao
 */
@WebServlet(name = "ForeServlet")
public class ForeServlet extends BaseForeServlet {
    Logger logger = Logger.getLogger(ForeServlet.class);

    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = new CategoryDAO().list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        logger.debug("Testas;ldjqwoikeqwkjlelasdnbasnjdbasd");
        logger.debug(cs.toString());
        return "home.jsp";
    }
}

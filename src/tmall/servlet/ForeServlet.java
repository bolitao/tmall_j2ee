package tmall.servlet;

import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import tmall.bean.Category;
import tmall.bean.User;
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

    /**
     * 返回首页数据
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转链接
     */
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs = new CategoryDAO().list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        logger.debug(cs.toString());
        System.out.println(cs.toString());
        return "home.jsp";
    }

    /**
     * 提供用户注册
     *
     * @param request
     * @param response
     * @param page
     * @return
     */
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String username = request.getParameter("name");
        String password = request.getParameter("password");
        username = HtmlUtils.htmlEscape(username);
        logger.debug(username);
        if (userDAO.isExist(username)) {
            request.setAttribute("msg", "用户名已经被使用");
            return "register.jsp";
        }
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        logger.debug("Username: " + username + ", password: " + password);
        userDAO.add(user);
        return "@registerSuccess.jsp";
    }
}

package tmall.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;
import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

/**
 * @author Boli Tao
 */
@WebFilter(filterName = "ForeServletFilter")
public class ForeServletFilter implements Filter {
    Logger logger = Logger.getLogger(ForeServletFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String contextPath = request.getServletContext().getContextPath();
        request.getServletContext().setAttribute("contextPath", contextPath);
        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if (null != user) {
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem oi : ois) {
                cartTotalItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if (null == cs) {
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        if (uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            String method = StringUtils.substringAfterLast(uri, "/fore");
            // logger.debug(method);
            request.setAttribute("method", method);
            req.getRequestDispatcher("/foreServlet").forward(request, response);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}

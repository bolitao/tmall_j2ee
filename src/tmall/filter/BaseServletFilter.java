package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Boli Tao
 */
@WebFilter(filterName = "BaseServletFilter")
public class BaseServletFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String contextPath = request.getServletContext().getContextPath();
        // System.out.println("ContextPath: " + contextPath);
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri, contextPath);
        // System.out.println(uri);
        if ("/index".equals(uri)) {
            ((HttpServletResponse) resp).sendRedirect("./admin_category_list");
            return;
        }
        if (uri.startsWith("/admin_")) {
            // 截取 uri 中间部分，以获得跳转 servlet
            String servletPath = StringUtils.substringBetween(uri, "_", "_") + "Servlet";
            // 截取 uri 中 _ 后面的部分，以获取对应的方法
            String method = StringUtils.substringAfterLast(uri, "_");
            request.setAttribute("method", method);
            request.getRequestDispatcher(servletPath).forward(request, response);
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}

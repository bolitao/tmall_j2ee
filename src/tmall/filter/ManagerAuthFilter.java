package tmall.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * TODO: manager login auth
 *
 * @author Boli Tao
 */
@WebFilter(filterName = "ManagerAuthFilter")
public class ManagerAuthFilter implements Filter {
    Logger logger = Logger.getLogger(ManagerAuthFilter.class);
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}

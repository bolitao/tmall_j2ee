package tmall.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tmall.dao.*;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author Boli Tao
 */
@WebServlet(name = "BaseBackServlet")
public abstract class BaseBackServlet extends HttpServlet {
    /**
     * add method of end-management
     *
     * @param request  request
     * @param response response
     * @param page     page info
     * @return redirect
     */
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page);

    /**
     * delete method of end-management
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return redirect
     */
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page);

    /**
     * edit method of end-management
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return redirect
     */
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page);

    /**
     * update method of end-management
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return redirect
     */
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page);

    /**
     * query method of end-management
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return redirect
     */
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page);

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // by default, index value is 0 and 5 pieces of data per page
            int start = 0;
            int count = 5;
            try {
                start = Integer.parseInt(req.getParameter("page.start"));
            } catch (Exception ignored) {
            }
            try {
                count = Integer.parseInt(req.getParameter("page.count"));
            } catch (Exception ignored) {
            }
            Page page = new Page(start, count);
            String method = (String) req.getAttribute("method");
            // TODO: reflex
            Method m = this.getClass().getMethod(method, javax.servlet.http.HttpServletRequest.class,
                    javax.servlet.http.HttpServletResponse.class, Page.class);
            String redirect = m.invoke(this, req, resp, page).toString();
            // do specific things according to the value of redirect
            if (redirect.startsWith("@")) {
                // send redirect
                resp.sendRedirect(redirect.substring(1));
            } else if (redirect.startsWith("%")) {
                // print
                resp.getWriter().print(redirect.substring(1));
            } else {
                // server-end forward
                req.getRequestDispatcher(redirect).forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
        InputStream is = null;
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 10M limit of upload
            factory.setSizeThreshold(10 * 1024 * 1024);
            List items = upload.parseRequest(request);
            for (Object o : items) {
                FileItem item = (FileItem) o;
                // if return value of item.isFormField is true, it means item is normal form (not file)
                if (!item.isFormField()) {
                    is = item.getInputStream();
                } else {
                    String paramName = item.getFieldName();
                    String paramValue = item.getString();
                    paramValue = new String(paramValue.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                    params.put(paramName, paramValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
}

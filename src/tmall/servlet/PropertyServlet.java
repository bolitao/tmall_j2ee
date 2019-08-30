package tmall.servlet;

import org.apache.log4j.Logger;
import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * TODO: 梳理多对一关系
 *
 * @author Boli Tao
 */
@WebServlet(name = "PropertyServlet")
public class PropertyServlet extends BaseBackServlet {
    private static final Logger logger = Logger.getLogger(PropertyServlet.class);

    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        Property property = new Property();
        property.setCategory(category);
        String name = request.getParameter("name");
        property.setName(name);
        propertyDAO.add(property);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property property = propertyDAO.get(id);
        int cid = property.getCategory().getId();
        propertyDAO.delete(id);
        return "@admin_property_list?cid=" + cid;
    }

    /**
     * 跳转到编辑页面的方法
     *
     * @param request  request
     * @param response response
     * @param page     page info
     * @return redirect link
     */
    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        request.setAttribute("p", p);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Property property = new Property();
        property.setCategory(category);
        property.setId(id);
        property.setName(name);
        propertyDAO.update(property);
        return "@admin_property_list?cid=" + cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        // logger.debug("cid: " + cid + ", page.start: " + page.getStart() + ", page.count: " + page.getCount());
        Category category = categoryDAO.get(cid);
        List<Property> propertyList = propertyDAO.list(cid, page.getStart(), page.getCount());
        // logger.debug("Debug: " + propertyList.toString());
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        // logger.debug("Debug: total: " + total);
        // parameter 中 cid 表示某一个分类
        page.setParam("&cid=" + cid);
        request.setAttribute("c", category);
        request.setAttribute("ps", propertyList);
        request.setAttribute("page", page);
        return "admin/listProperty.jsp";
    }
}

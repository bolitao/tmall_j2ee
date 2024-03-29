package tmall.servlet;

import tmall.bean.Order;
import tmall.dao.OrderDAO;
import tmall.util.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author Boli Tao
 */
@WebServlet(name = "OrderServlet")
public class OrderServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Order> os = orderDAO.list(page.getStart(), page.getCount());
        orderItemDAO.fill(os);
        int total = orderDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("os", os);
        request.setAttribute("page", page);
        return "admin/listOrder.jsp";
    }

    /**
     * 设置发货标记
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return redirect info
     */
    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderDAO.get(id);
        order.setDeliveryDate(new Date());
        order.setStatus(OrderDAO.waitConfirm);
        orderDAO.update(order);
        return "@admin_order_list";
    }
}

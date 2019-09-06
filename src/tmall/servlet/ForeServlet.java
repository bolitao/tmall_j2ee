package tmall.servlet;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.comparator.*;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        // logger.debug(cs.toString());
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
//        logger.debug(username);
        if (userDAO.isExist(username)) {
            request.setAttribute("msg", "用户名已经被使用");
            return "register.jsp";
        }
        User user = new User();
        user.setName(username);
        user.setPassword(password);
//        logger.debug("Username: " + username + ", password: " + password);
        userDAO.add(user);
        return "@registerSuccess.jsp";
    }

    /**
     * 前端登录
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String username = request.getParameter("name");
        username = HtmlUtils.htmlEscape(username);
        String password = request.getParameter("password");
        User user = userDAO.get(username, password);
        if (null == user) {
            request.setAttribute("msg", "账号密码错误");
            return "login.jsp";
        }
        request.getSession().setAttribute("user", user);
        return "@forehome";
    }

    /**
     * 前端用户登出
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    /**
     * 为 request 设置某项产品详细数据
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        List<ProductImage> productSingleImages = productImageDAO.list(product, ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(product, ProductImageDAO.type_detail);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);
        List<PropertyValue> propertyValues = propertyValueDAO.list(product.getId());
        List<Review> reviews = reviewDAO.list(product.getId());
        productDAO.setSaleAndReviewNumber(product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("p", product);
        request.setAttribute("pvs", propertyValues);
        return "product.jsp";
    }

    /**
     * 检查是否登录
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param page     page
     * @return 返回字符串
     */
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null != user) {
            return "%success";
        }
        return "%fail";
    }

    /**
     * 前端 Ajax 方式登录
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 登陆是否成功信息
     */
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userDAO.get(name, password);
        if (null == user) {
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success";
    }

    /**
     * 为 request 设置分类数据
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param page     page
     * @return 跳转信息
     */
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = new CategoryDAO().get(cid);
        new ProductDAO().fill(category);
        new ProductDAO().setSaleAndReviewNumber(category.getProducts());
        String sort = request.getParameter("sort");
        if (null != sort) {
            switch (sort) {
                case "review":
                    category.getProducts().sort(new ProductReviewComparator());
                    break;
                case "date":
                    category.getProducts().sort(new ProductDateComparator());
                    break;
                case "saleCount":
                    category.getProducts().sort(new ProductSaleCountComparator());
                    break;
                case "price":
                    category.getProducts().sort(new ProductPriceComparator());
                    break;
                case "all":
                    category.getProducts().sort(new ProductAllComparator());
                    break;
                default:
                    break;
            }
        }
        request.setAttribute("c", category);
        return "category.jsp";
    }

    /**
     * 前端搜索
     *
     * @param request  request
     * @param response response
     * @param page     response
     * @return 跳转信息
     */
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
        String keyword = request.getParameter("keyword");
        List<Product> ps = new ProductDAO().search(keyword, 0, 20);
        productDAO.setSaleAndReviewNumber(ps);
        request.setAttribute("ps", ps);
        return "searchResult.jsp";
    }

    /**
     * 用户直接点击购买某项产品
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        Product p = productDAO.get(pid);
        int orderItemId = 0;
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == p.getId()) {
                oi.setNumber(oi.getNumber() + num);
                orderItemDAO.update(oi);
                found = true;
                orderItemId = oi.getId();
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(p);
            orderItemDAO.add(orderItem);
            orderItemId = orderItem.getId();
        }
        return "@forebuy?oiid=" + orderItemId;
    }

    /**
     * 订单结算
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
        String[] parameterValues = request.getParameterValues("oiid");
        List<OrderItem> orderItemList = new ArrayList<>();
        float total = 0;
        for (String strid : parameterValues) {
            int oiid = Integer.parseInt(strid);
            OrderItem oi = orderItemDAO.get(oiid);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            orderItemList.add(oi);
        }
        request.getSession().setAttribute("ois", orderItemList);
        request.setAttribute("total", total);
        return "buy.jsp";
    }

    /**
     * 放入购物车
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 是否成功字符串
     */
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        int num = Integer.parseInt(request.getParameter("num"));
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> orderItemList = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : orderItemList) {
            if (oi.getProduct().getId() == product.getId()) {
                oi.setNumber(oi.getNumber() + num);
                orderItemDAO.update(oi);
                found = true;
                break;
            }
        }
        if (!found) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUser(user);
            orderItem.setNumber(num);
            orderItem.setProduct(product);
            orderItemDAO.add(orderItem);
        }
        return "%success";
    }

    /**
     * 购物车
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> orderItemList = orderItemDAO.listByUser(user.getId());
        request.setAttribute("ois", orderItemList);
        return "cart.jsp";
    }

    /**
     * 更改订单数量
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 操作结果
     */
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            return "%fail";
        }
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if (oi.getProduct().getId() == pid) {
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }

        }
        return "%success";
    }

    /**
     * 删除购物车中订单项
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 操作结果
     */
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            return "%fail";
        }
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }

    /**
     * 将订单项合为一个订单，设置订单属性
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转信息
     */
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
        if (ois.isEmpty()) {
            return "@login.jsp";
        }
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");
        Order order = new Order();
        // 订单号生成方式：当前时间+随机四位数
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);
        orderDAO.add(order);
        float total = 0;
        for (OrderItem oi : ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        return "@forealipay?oid=" + order.getId() + "&total=" + total;
    }

    /**
     * 跳转到 alipay.jsp
     *
     * @param request  request
     * @param response response
     * @param page     page
     * @return 跳转链接
     */
    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page) {
        return "alipay.jsp";
    }

    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        new OrderDAO().update(order);
        request.setAttribute("o", order);
        return "payed.jsp";
    }

    public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<Order> os = orderDAO.list(user.getId(), OrderDAO.delete);
        orderItemDAO.fill(os);
        request.setAttribute("os", os);
        return "bought.jsp";
    }

    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        request.setAttribute("o", o);
        return "confirmPay.jsp";
    }

    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        o.setConfirmDate(new Date());
        orderDAO.update(o);
        return "orderConfirmed.jsp";
    }

    public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.delete);
        orderDAO.update(o);
        return "%success";
    }

    public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewDAO.list(p.getId());
        productDAO.setSaleAndReviewNumber(p);
        request.setAttribute("p", p);
        request.setAttribute("o", o);
        request.setAttribute("reviews", reviews);
        return "review.jsp";
    }

    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.finish);
        orderDAO.update(o);
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        String content = request.getParameter("content");
        content = HtmlUtils.htmlEscape(content);
        User user = (User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setProduct(p);
        review.setCreateDate(new Date());
        review.setUser(user);
        reviewDAO.add(review);
        return "@forereview?oid=" + oid + "&showonly=true";
    }
}

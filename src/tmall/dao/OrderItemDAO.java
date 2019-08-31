package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

/**
 * @author boli
 */
public class OrderItemDAO {
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "select count(*) from orderitem";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(OrderItem bean) {

        String sql = "insert into orderitem values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, bean.getProduct().getId());
            //订单项在创建的时候，是没有订单信息的
            if (null == bean.getOrder()) {
                ps.setInt(2, -1);
            } else {
                ps.setInt(2, bean.getOrder().getId());
            }
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OrderItem bean) {
        String sql = "update orderitem set pid= ?, oid=?, uid=?,number=?  where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, bean.getProduct().getId());
            if (null == bean.getOrder()) {
                ps.setInt(2, -1);
            } else {
                ps.setInt(2, bean.getOrder().getId());
            }
            ps.setInt(3, bean.getUser().getId());
            ps.setInt(4, bean.getNumber());
            ps.setInt(5, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "delete from orderitem where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public OrderItem get(int id) {
        OrderItem bean = new OrderItem();
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "select * from orderitem where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where uid = ? and oid=-1 order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<OrderItem> listByOrder(int oid) {
        return listByOrder(oid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where oid = ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, oid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid, 0, Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where pid = ? order by id desc limit ?,? ";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, pid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();
                int id = rs.getInt(1);
                int uid = rs.getInt("uid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");
                Product product = new ProductDAO().get(pid);
                if (-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);
                bean.setProduct(product);
                bean.setUser(user);
                bean.setNumber(number);
                bean.setId(id);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public int getSaleCount(int pid) {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "select sum(number) from orderitem where pid = " + pid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * 设置订单项集合
     *
     * @param orderList 订单集合
     */
    public void fill(List<Order> orderList) {
        for (Order order : orderList) {
            List<OrderItem> orderItemList = listByOrder(order.getId());
            float total = 0;
            int totalNumber = 0;
            for (OrderItem orderItem : orderItemList) {
                total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
                totalNumber += orderItem.getNumber();
            }
            order.setTotal(total);
            order.setOrderItems(orderItemList);
            order.setTotalNumber(totalNumber);
        }
    }

    /**
     * 把各订单形成为订单项
     *
     * @param order 订单
     */
    public void fill(Order order) {
        List<OrderItem> orderItemList = listByOrder(order.getId());
        float total = 0;
        for (OrderItem orderItem : orderItemList) {
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
        }
        order.setTotal(total);
        order.setOrderItems(orderItemList);
    }
}

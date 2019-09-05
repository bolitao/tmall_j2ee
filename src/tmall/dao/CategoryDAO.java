package tmall.dao;

import tmall.bean.Category;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author boli
 */
public class CategoryDAO {
    public int getTotal() {
        int total = 0;
        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement()
        ) {
            String sql = "SELECT COUNT(*) FROM category";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public void add(Category bean) {
        String sql = "INSERT INTO category VALUES(null, ?)";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bean.getName());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                // 在这里给传入的 bean 设置 id 目的是之后对 bean 继续进行操作
                // 比如 CategoryServlet 中的 add 方法就使用到了 bean 的 getID() 方法
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Category bean) {
        String sql = "UPDATE category SET name= ? WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bean.getName());
            statement.setInt(2, bean.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM category WHERE id = " + id;
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Category get(int id) {
        Category category = null;
        try (Connection connection = DBUtil.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "SELECT * FROM category WHERE id = " + id;
            ResultSet set = statement.executeQuery(sql);
            if (set.next()) {
                category = new Category();
                String name = set.getString(2);
                category.setName(name);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    /**
     * Query categories by given start and count
     *
     * @param start start
     * @param count count
     * @return category results order by id
     */
    public List<Category> list(int start, int count) {
        List<Category> categoryList = new ArrayList<Category>();
        String sql = "SELECT * FROM category ORDER BY id DESC limit ?, ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, start);
            statement.setInt(2, count);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Category category = new Category();
                int id = set.getInt(1);
                String name = set.getString(2);
                category.setId(id);
                category.setName(name);
                categoryList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    /**
     * Query all category data
     *
     * @return all category result
     */
    public List<Category> list() {
        return list(0, Short.MAX_VALUE);
    }
}

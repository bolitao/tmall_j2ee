package tmall.dao;

import tmall.util.DBUtil;

import java.sql.*;

/**
 * @author Boli Tao
 */
public class ManagerDAO {
    public String getPassword(String username) {
        String password = null;
        String sql = "SELECT password FROM manager WHERE username = ?";
        try (Connection connection = DBUtil.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                password = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }
}

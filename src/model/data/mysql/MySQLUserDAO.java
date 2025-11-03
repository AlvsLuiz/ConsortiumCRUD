package model.data.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Post;
import model.User;
import model.Consortium;
import model.data.DAOUtils;
import model.data.UserDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLUserDAO implements UserDAO {

    @Override
    public void save(User user) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sql = "INSERT INTO market_user (user_name, active_consortium, email, age) VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getConsortium().toString());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getAge());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao inserir usuário no BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void update(User user) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sql = "UPDATE market_user SET user_name = ?, active_consortium = ?, email = ?, age = ? WHERE user_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getConsortium().toString());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getAge());
            ps.setInt(5, user.getId());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao atualizar usuário no BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void delete(User user) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sql = "DELETE FROM market_user WHERE user_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao deletar usuário do BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public List<User> findAll() throws ModelException {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        List<User> usersList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();
            st = connection.createStatement();

            String sql = "SELECT * FROM market_user ORDER BY user_name";
            rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("user_name");
                String consortiumStr = rs.getString("active_consortium");
                Consortium consortium = consortiumStr.equals("S") ? Consortium.S : Consortium.N;
                String email = rs.getString("email");
                int age = rs.getInt("age");

                User user = new User(id);
                user.setName(name);
                user.setConsortium(consortium);
                user.setEmail(email);
                user.setAge(age);

                List<Post> posts = new MySQLPostDAO().findByUserId(id);
                user.setPosts(posts);

                usersList.add(user);
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar usuários do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(st);
            DAOUtils.close(connection);
        }

        return usersList;
    }

    @Override
    public User findById(int id) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sql = "SELECT * FROM market_user WHERE user_id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("user_name");
                String consortiumStr = rs.getString("active_consortium");
                Consortium consortium = consortiumStr.equals("S") ? Consortium.S : Consortium.N;
                String email = rs.getString("email");
                int age = rs.getInt("age");

                user = new User(id);
                user.setName(name);
                user.setConsortium(consortium);
                user.setEmail(email);
                user.setAge(age);
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao buscar usuário por ID no BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }

        return user;
    }
}

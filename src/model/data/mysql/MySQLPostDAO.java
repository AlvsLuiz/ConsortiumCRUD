package model.data.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ModelException;
import model.Post;
import model.User;
import model.Cars;
import model.data.DAOFactory;
import model.data.DAOUtils;
import model.data.PostDAO;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLPostDAO implements PostDAO {

    @Override
    public void save(Post post) throws ModelException {    
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlInsert = "INSERT INTO posts (content, post_date, user_id, car_id) VALUES (?, CURDATE(), ?, ?)";
            preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setInt(2, post.getUser().getId());
            preparedStatement.setInt(3, post.getCar().getId()); // ✅ Adicionado car_id

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao inserir post no BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void update(Post post) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlUpdate = "UPDATE posts SET content = ?, user_id = ?, car_id = ?, post_date = CURDATE() WHERE post_id = ?";
            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setInt(2, post.getUser().getId());
            preparedStatement.setInt(3, post.getCar().getId()); // ✅ Corrigido car_id
            preparedStatement.setInt(4, post.getId());          // ✅ Adicionado post_id

            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao atualizar post no BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void delete(Post post) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = MySQLConnectionFactory.getConnection();  

            String sqlDelete = "DELETE FROM posts WHERE post_id = ?";
            preparedStatement = connection.prepareStatement(sqlDelete);
            preparedStatement.setInt(1, post.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao excluir post do BD.", sqle);
        } finally {
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }
    }

    @Override
    public List<Post> findAll() throws ModelException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Post> postsList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();
            statement = connection.createStatement();
            String sqlSelect = "SELECT * FROM posts ORDER BY post_date DESC";
            rs = statement.executeQuery(sqlSelect);

            setUpUsers(rs, postsList);
        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar posts do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(statement);
            DAOUtils.close(connection);
        }

        return postsList;
    }

    @Override
    public List<Post> findByUserId(int userId) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Post> postsList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();
            String sqlSelect = "SELECT * FROM posts WHERE user_id = ?";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, userId);
            rs = preparedStatement.executeQuery();

            setUpUsers(rs, postsList);
        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar posts do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return postsList;
    }

    @Override
    public List<Post> findByCarId(int carId) throws ModelException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Post> postsList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();
            String sqlSelect = "SELECT * FROM posts WHERE car_id = ?";
            preparedStatement = connection.prepareStatement(sqlSelect);
            preparedStatement.setInt(1, carId);
            rs = preparedStatement.executeQuery();

            setUpUsers(rs, postsList);
        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar posts do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(preparedStatement);
            DAOUtils.close(connection);
        }

        return postsList;
    }

    private void setUpUsers(ResultSet rs, List<Post> postsList)
            throws SQLException, ModelException {

        while (rs.next()) {
            int postId = rs.getInt("post_id");
            String postContent = rs.getString("content");
            Date postDate = rs.getDate("post_date");
            int userId = rs.getInt("user_id");
            int carId = rs.getInt("car_id"); // ✅ Adicionado

            Post newPost = new Post(postId);
            newPost.setContent(postContent);
            newPost.setDate(postDate);

            // Carregar usuário e carro
            User postUser = DAOFactory.createUserDAO().findById(userId);
            Cars postCar = DAOFactory.createCarsDAO().findByCarId(carId);

            newPost.setUser(postUser);
            newPost.setCar(postCar); // ✅ Associar o carro

            postsList.add(newPost);
        }
    }
}

package model.data.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Cars;
import model.ModelException;
import model.Post;
import model.data.CarsDAO;
import model.data.DAOUtils;
import model.data.mysql.utils.MySQLConnectionFactory;

public class MySQLCarsDAO implements CarsDAO {

    @Override
    public void save(Cars car) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlInsert = "INSERT INTO cars (make, car_name, year_model, plate) VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(sqlInsert);
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYearModel());
            ps.setString(4, car.getPlate());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao inserir carro no BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void update(Cars car) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlUpdate = "UPDATE cars SET make = ?, car_name = ?, year_model = ?, plate = ? WHERE car_id = ?";
            ps = connection.prepareStatement(sqlUpdate);
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYearModel());
            ps.setString(4, car.getPlate());
            ps.setInt(5, car.getId());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao atualizar carro no BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public void delete(Cars car) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlDelete = "DELETE FROM cars WHERE car_id = ?";
            ps = connection.prepareStatement(sqlDelete);
            ps.setInt(1, car.getId());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao deletar carro no BD.", sqle);
        } finally {
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }
    }

    @Override
    public List<Cars> findAll() throws ModelException {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        List<Cars> carsList = new ArrayList<>();

        try {
            connection = MySQLConnectionFactory.getConnection();
            st = connection.createStatement();
            String sqlSelect = "SELECT * FROM cars ORDER BY car_name";
            rs = st.executeQuery(sqlSelect);

            while (rs.next()) {
                int id = rs.getInt("car_id");
                String make = rs.getString("make");
                String carName = rs.getString("car_name");
                int yearModel = rs.getInt("year_model");
                String plate = rs.getString("plate");

                Cars car = new Cars(id);
                car.setMake(make);
                car.setModel(carName);
                car.setYearModel(yearModel);
                car.setPlate(plate);

                List<Post> posts = new MySQLPostDAO().findByCarId(id);
                car.setPosts(posts);

                carsList.add(car);
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao carregar carros do BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(st);
            DAOUtils.close(connection);
        }

        return carsList;
    }

    @Override
    public Cars findByCarId(int id) throws ModelException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cars car = null;

        try {
            connection = MySQLConnectionFactory.getConnection();

            String sqlSelect = "SELECT * FROM cars WHERE car_id = ?";
            ps = connection.prepareStatement(sqlSelect);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                String make = rs.getString("make");
                String carName = rs.getString("car_name");
                int yearModel = rs.getInt("year_model");
                String plate = rs.getString("plate");

                car = new Cars(id);
                car.setMake(make);
                car.setModel(carName);
                car.setYearModel(yearModel);
                car.setPlate(plate);
            }

        } catch (SQLException sqle) {
            DAOUtils.sqlExceptionTreatement("Erro ao buscar carro por ID no BD.", sqle);
        } finally {
            DAOUtils.close(rs);
            DAOUtils.close(ps);
            DAOUtils.close(connection);
        }

        return car;
    }
}

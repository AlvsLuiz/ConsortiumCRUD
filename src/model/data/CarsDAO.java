package model.data;

import java.util.List;

import model.Cars;
import model.ModelException;

public interface CarsDAO {
	void save(Cars car) throws ModelException;
	void update(Cars car) throws ModelException;
	void delete(Cars car) throws ModelException;
	List<Cars> findAll() throws ModelException;
	Cars findByCarId(int id) throws ModelException;
}

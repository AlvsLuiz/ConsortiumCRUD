package controller;

import java.util.List;

import model.Cars;
import model.ModelException;
import model.data.CarsDAO;
import model.data.DAOFactory;
import view.swing.cars.ICarFormView;
import view.swing.cars.ICarListView;

public class CarsController {
	private final CarsDAO carsDAO = DAOFactory.createCarsDAO();
	private ICarListView carListView;
	private ICarFormView carFormView;
	
	public void loadCars() {
		try {
			List<Cars> cars = carsDAO.findAll();
			carListView.setCarList(cars);			
		} catch (ModelException e) {
            carListView.showMessage("Erro ao carregar os carros: " + e.getMessage());
        }
	}
	
	public void saveOrUpdateCars(boolean isNew) {
		Cars car = carFormView.getCarsFromForm();
		
		try {
			car.validate();
		} catch (IllegalArgumentException e) {
            carFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }
		
		try {
			if(isNew) {
				carsDAO.save(car);
				carFormView.showInfoMessage("Carro salvo com sucesso");
			} else {
				carsDAO.update(car);
				carFormView.showInfoMessage("Carro atualizado com sucesso");
			}
			carFormView.close();
		} catch ( ModelException e) {
			carFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
		}
	}
	
	public void deleteCars(Cars car) {
		try {
			carsDAO.delete(car);
			carListView.showMessage("Carro excluido");
			loadCars();
		} catch (ModelException e) {
            carListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
	}
	
	public void setCarListView(ICarListView carListView) {
		this.carListView = carListView;
	}
	
	public void setCarFormView(ICarFormView carFormView) {
		this.carFormView = carFormView;
	}
}

package view.swing.cars;

import java.util.List;

import model.Cars;

public interface ICarListView {
	void setCarList(List<Cars> cars);
	void showMessage(String msg);
}

package view.swing.cars;

import model.Cars;
public interface ICarFormView {
	Cars getCarsFromForm();
	void setCarInForm(Cars car);
	void showInfoMessage(String msg);
	void showErrorMessage(String msg);
	void close();
}

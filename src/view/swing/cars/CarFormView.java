package view.swing.cars;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.CarsController;
import model.Cars;

public class CarFormView extends JDialog implements ICarFormView {
    private final JTextField makeField = new JTextField(20);
    private final JTextField modelField = new JTextField(20);
    private final JTextField yearModelField = new JTextField(20);
    private final JTextField plateField = new JTextField(20);
    private final JButton saveButton = new JButton("Salvar");
    private final JButton closeButton = new JButton("Fechar");
    private final CarsController controller;
    private final boolean isNew;
    private final CarListView parent;
    private Cars car;

    public CarFormView(CarListView parent, Cars car, CarsController controller) {
        super(parent, true);
        this.parent = parent;
        this.controller = controller;
        this.controller.setCarFormView(this);
        this.car = car;
        this.isNew = (car == null);

        setTitle(isNew ? "Novo Carro" : "Editar Carro");
        setSize(400, 280);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        add(makeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        add(modelField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Ano do Modelo:"), gbc);
        gbc.gridx = 1;
        add(yearModelField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        add(plateField, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) setCarInForm(car);

        saveButton.addActionListener(e -> controller.saveOrUpdateCars(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public Cars getCarsFromForm() {
        if (car == null) car = new Cars(0);
        car.setMake(makeField.getText());
        car.setModel(modelField.getText());
        try {
            car.setYearModel(Integer.parseInt(yearModelField.getText()));
        } catch (NumberFormatException e) {
            showErrorMessage("Ano inválido! Use apenas números.");
            return null;
        }
        car.setPlate(plateField.getText());
        return car;
    }

    @Override
    public void setCarInForm(Cars car) {
        makeField.setText(car.getMake());
        modelField.setText(car.getModel());
        yearModelField.setText(String.valueOf(car.getYearModel()));
        plateField.setText(car.getPlate());
    }

    @Override
    public void showInfoMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void close() {
        parent.refresh();
        dispose();
    }
}

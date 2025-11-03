package view.swing.cars;

import controller.CarsController;
import model.Cars;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarListView extends JDialog implements ICarListView {
    private CarsController controller;
    private final CarsTableModel tableModel = new CarsTableModel();
    private final JTable table = new JTable(tableModel);

    public CarListView(JFrame parent) {
        super(parent, "Carros", true);
        this.controller = new CarsController();
        this.controller.setCarListView(this);

        setSize(700, 400);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            CarFormView form = new CarFormView(this, null, controller);
            form.setVisible(true);
        });

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Cars car = tableModel.getCarAt(row);
                CarFormView form = new CarFormView(this, car, controller);
                form.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um carro para editar.");
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Cars car = tableModel.getCarAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o carro?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteCars(car);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um carro para excluir.");
            }
        });

        controller.loadCars();
    }

    @Override
    public void setCarList(List<Cars> cars) {
        tableModel.setCars(cars);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadCars();
    }

    static class CarsTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Marca", "Modelo", "Ano", "Placa"};
        private List<Cars> cars = new ArrayList<>();

        public void setCars(List<Cars> cars) {
            this.cars = cars;
            fireTableDataChanged();
        }

        public Cars getCarAt(int row) {
            return cars.get(row);
        }

        @Override public int getRowCount() { return cars.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Cars c = cars.get(row);
            switch (col) {
                case 0: return c.getId();
                case 1: return c.getMake();
                case 2: return c.getModel();
                case 3: return c.getYearModel();
                case 4: return c.getPlate();
                default: return null;
            }
        }

        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}

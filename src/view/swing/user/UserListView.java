package view.swing.user;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UserListView extends JDialog implements IUserListView {
    private UserController controller;
    private final UserTableModel tableModel = new UserTableModel();
    private final JTable table = new JTable(tableModel);

    public UserListView(JFrame parent) {
        super(parent, "Usuários", true);
        this.controller = new UserController();
        this.controller.setUserListView(this);

        setSize(650, 400);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Botões
        addButton.addActionListener(e -> {
            UserFormView form = new UserFormView(this, null, controller);
            form.setVisible(true);
        });

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                User user = tableModel.getUserAt(row);
                UserFormView form = new UserFormView(this, user, controller);
                form.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.");
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                User user = tableModel.getUserAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Deseja realmente excluir o usuário?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.excluirUsuario(user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.");
            }
        });

        controller.loadUsers();
    }

    @Override
    public void setUserList(List<User> users) {
        tableModel.setUsers(users);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadUsers();
    }

    static class UserTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Nome", "Consórcio", "Email", "Idade"};
        private List<User> users = new ArrayList<>();

        public void setUsers(List<User> users) {
            this.users = users;
            fireTableDataChanged();
        }

        public User getUserAt(int row) {
            return users.get(row);
        }

        @Override public int getRowCount() { return users.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            User u = users.get(row);
            switch (col) {
                case 0: return u.getId();
                case 1: return u.getName();
                case 2: return u.getConsortium();
                case 3: return u.getEmail();
                case 4: return u.getAge();
                default: return null;
            }
        }

        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}

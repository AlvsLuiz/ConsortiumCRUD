package view.swing.user;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.UserController;
import model.Consortium;
import model.User;

class UserFormView extends JDialog implements IUserFormView {
    private final JTextField nameField = new JTextField(20);
    private final JComboBox<String> consortiumBox = new JComboBox<>(new String[]{"S", "N"});
    private final JTextField emailField = new JTextField(20);
    private final JTextField ageField = new JTextField(5); 
    private final JButton saveButton = new JButton("Salvar");
    private final JButton closeButton = new JButton("Fechar");
    private UserController controller;
    private final boolean isNew;
    private final UserListView parent;
    private User user;

    public UserFormView(UserListView parent, User user, UserController controller) {
        super(parent, true);
        this.controller = controller;
        this.controller.setUserFormView(this);

        this.parent = parent;
        this.user = user;
        this.isNew = (user == null);

        setTitle(isNew ? "Novo Usuário" : "Editar Usuário");
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Consorcio:"), gbc);
        gbc.gridx = 1;
        add(consortiumBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1;
        add(ageField, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) setUserInForm(user);

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public User getUserFromForm() {
        if (user == null) user = new User(0);
        user.setName(nameField.getText());
        user.setConsortium(consortiumBox.getSelectedItem().toString().equals("S") ? Consortium.S : Consortium.N);
        user.setEmail(emailField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        return user;
    }

    @Override
    public void setUserInForm(User user) {
        nameField.setText(user.getName());
        consortiumBox.setSelectedItem(user.getConsortium().toString());
        emailField.setText(user.getEmail());
        ageField.setText(String.valueOf(user.getAge()));
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

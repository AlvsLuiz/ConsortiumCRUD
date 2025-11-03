package view.swing.post;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import controller.PostController;
import model.Post;

public class PostListView extends JDialog implements IPostListView {
    private PostController controller;
    private final PostTableModel tableModel = new PostTableModel();
    private final JTable table = new JTable(tableModel);

    public PostListView(JFrame parent) {
        super(parent, "Posts", true);
        this.controller = new PostController();
        this.controller.setPostListView(this);

        setSize(800, 400);
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
            PostFormView form = new PostFormView(this, null, controller);
            form.setVisible(true);
        });

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Post post = tableModel.getPostAt(row);
                PostFormView form = new PostFormView(this, post, controller);
                form.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um post para editar.");
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Post post = tableModel.getPostAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Deseja excluir o post?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.excluirPost(post);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um post para excluir.");
            }
        });

        controller.loadPosts();
    }

    @Override
    public void setPostList(List<Post> posts) {
        tableModel.setPosts(posts);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadPosts();
    }

    static class PostTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Conteúdo", "Data", "Usuário", "Carro"};
        private List<Post> posts = new ArrayList<>();

        public void setPosts(List<Post> posts) {
            this.posts = posts;
            fireTableDataChanged();
        }

        public Post getPostAt(int row) {
            return posts.get(row);
        }

        @Override public int getRowCount() { return posts.size(); }

        @Override public int getColumnCount() { return columns.length; }

        @Override public String getColumnName(int col) { return columns[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Post p = posts.get(row);
            switch (col) {
                case 0: return p.getId();
                case 1: return p.getContent();
                case 2: return p.getDate();
                case 3: return p.getUser().getName();
                case 4: return p.getCar().getModel();
                default: return null;
            }
        }

        @Override public boolean isCellEditable(int row, int col) { return false; }
    }
}

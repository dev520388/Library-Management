package com.example.library.ui;

import com.example.library.dao.BookDAO;
import com.example.library.model.Book;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainApp extends JFrame {
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField yearField;
    private JComboBox<String> statusComboBox;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private BookDAO bookDAO;
    private int selectedBookId = -1;

    public MainApp() {
        bookDAO = new BookDAO();
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 500));

        JPanel contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(15,15,15,15));
        setContentPane(contentPane);

        // Form Panel (for Add/Update)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        // Publisher
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        publisherField = new JTextField(20);
        formPanel.add(publisherField, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        yearField = new JTextField(20);
        formPanel.add(yearField, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        statusComboBox = new JComboBox<>(new String[] {"Available", "Borrowed", "Reserved"});
        formPanel.add(statusComboBox, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();

        JButton addButton = new JButton("Add Book");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.white);
        addButton.setFocusPainted(false);

        JButton updateButton = new JButton("Update Book");
        updateButton.setBackground(new Color(70, 130, 180));
        updateButton.setForeground(Color.white);
        updateButton.setFocusPainted(false);

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.setBackground(new Color(178, 34, 34));
        deleteButton.setForeground(Color.white);
        deleteButton.setFocusPainted(false);

        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(128, 128, 128));
        clearButton.setForeground(Color.white);
        clearButton.setFocusPainted(false);

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(buttonsPanel, gbc);

        contentPane.add(formPanel, BorderLayout.WEST);

        // Table Panel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "Publisher", "Year", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disable cell editing
            }
        };
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.getTableHeader().setReorderingAllowed(false);
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Library Books"));
        contentPane.add(scrollPane, BorderLayout.CENTER);

        loadBooks();

        // Action listeners
        addButton.addActionListener(e -> {
            if (validateForm()) {
                addBook();
            }
        });

        updateButton.addActionListener(e -> {
            if (selectedBookId != -1 && validateForm()) {
                updateBook();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedBookId != -1) {
                deleteBook();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> clearForm());

        booksTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedBookId = (int) tableModel.getValueAt(selectedRow, 0);
                    titleField.setText((String) tableModel.getValueAt(selectedRow, 1));
                    authorField.setText((String) tableModel.getValueAt(selectedRow, 2));
                    publisherField.setText((String) tableModel.getValueAt(selectedRow, 3));
                    yearField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
                    statusComboBox.setSelectedItem((String) tableModel.getValueAt(selectedRow, 5));
                }
            }
        });
    }

    // Load books from database in table
    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getYear(),
                book.getStatus()
            });
        }
        selectedBookId = -1;
    }

    private void addBook() {
        try {
            Book book = new Book(
                titleField.getText().trim(),
                authorField.getText().trim(),
                publisherField.getText().trim(),
                Integer.parseInt(yearField.getText().trim()),
                (String) statusComboBox.getSelectedItem()
            );
            boolean success = bookDAO.addBook(book);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book added successfully.");
                loadBooks();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid integer.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            Book book = new Book(
                selectedBookId,
                titleField.getText().trim(),
                authorField.getText().trim(),
                publisherField.getText().trim(),
                Integer.parseInt(yearField.getText().trim()),
                (String) statusComboBox.getSelectedItem()
            );
            boolean success = bookDAO.updateBook(book);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
                loadBooks();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a valid integer.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteBook() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookDAO.deleteBook(selectedBookId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                loadBooks();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a book title.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the author's name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!yearField.getText().trim().matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 4-digit year.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
        titleField.setText("");
        authorField.setText("");
        publisherField.setText("");
        yearField.setText("");
        statusComboBox.setSelectedIndex(0);
        booksTable.clearSelection();
        selectedBookId = -1;
    }

    public static void main(String[] args) {
        // Set system look and feel for better visuals
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored){}

        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}

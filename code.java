import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface {
    private Library library;

    public UserInterface(Library library) {
        this.library = library;
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel label = new JLabel("Enter Book Title:");
        label.setBounds(10, 20, 150, 25);
        frame.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(150, 20, 200, 25);
        frame.add(textField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(10, 60, 100, 25);
        frame.add(searchButton);

        JButton borrowButton = new JButton("Borrow");
        borrowButton.setBounds(120, 60, 100, 25);
        frame.add(borrowButton);

        JButton returnButton = new JButton("Return");
        returnButton.setBounds(230, 60, 100, 25);
        frame.add(returnButton);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(10, 100, 360, 150);
        frame.add(textArea);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textField.getText();
                Book book = library.searchBook(title);
                if (book != null) {
                    textArea.setText("Found: " + book.getTitle() + " by " + book.getAuthor());
                } else {
                    textArea.setText("Book not found or not available.");
                }
            }
        });

        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textField.getText();
                Book book = library.searchBook(title);
                if (book != null && library.borrowBook(book)) {
                    textArea.setText("You borrowed: " + book.getTitle());
                } else {
                    textArea.setText("Book not available for borrowing.");
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = textField.getText();
                Book book = library.searchBook(title);
                if (book != null) {
                    library.returnBook(book);
                    textArea.setText("You returned: " + book.getTitle());
                } else {
                    textArea.setText("Book not found.");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Library library = new Library();
        // Add sample books
        library.addBook(new Novel("The Great Gatsby", "F. Scott Fitzgerald", "123456789", "Fiction", "Adults"));
        library.addBook(new Textbook("Introduction to Java", "John Doe", "987654321", "CS101", "Computer Science"));

        new UserInterface(library);
    }
}

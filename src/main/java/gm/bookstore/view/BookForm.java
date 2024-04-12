package gm.bookstore.view;

import gm.bookstore.model.Book;
import gm.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class BookForm extends JFrame {

    BookService bookService;
    private JPanel panel;
    private JTable tableBook;
    private JTextField idText;
    private JTextField bookText;
    private JTextField authorText;
    private JTextField priceText;
    private JTextField stockText;
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private DefaultTableModel tableModelBooks;

    @Autowired
    public BookForm(BookService bookService) {
        this.bookService = bookService;
        initForm();
        addButton.addActionListener(e -> addBook());
        tableBook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                loadSelectedBook();
            }
        });
        modifyButton.addActionListener(e -> modifyBook());

        deleteButton.addActionListener(e -> deleteBook());
    }

    private void initForm(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900, 700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth() / 2);
        int y = (screenSize.height - getHeight() / 2);
        setLocation(x, y);
    }

    private void addBook(){
        // Leer los valores del form
        if(bookText.getText().equals("")) {
            showMessage("Provide name of book");
            bookText.requestFocusInWindow();
            return;
        }
        var nameBook = bookText.getText();
        var author = authorText.getText();
        var price = Double.parseDouble(priceText.getText());
        var stock = Integer.parseInt(stockText.getText());

        // Crear el objeto libro
        var book = new Book(null, nameBook, author, price, stock);
//        book.setTitle(nameBook);
//        book.setAuthor(author);
//        book.setPrice(price);
//        book.setStock(stock);
        this.bookService.saveBook(book);
        showMessage("Book added...");
        cleanForm();
        listBooks();
    }

    private void loadSelectedBook() {
        // Los índices de las columnas inician en 0
        var row = tableBook.getSelectedRow();
        if(row != -1) {     // Devuelve -1 si no se seleccionó ningun registro
            String idBook = tableBook.getModel().getValueAt(row, 0).toString();
            idText.setText(idBook);
            String nameBook = tableBook.getModel().getValueAt(row, 1).toString();
            bookText.setText(nameBook);
            String author = tableBook.getModel().getValueAt(row, 2).toString();
            authorText.setText(author);
            String price = tableBook.getModel().getValueAt(row, 3).toString();
            priceText.setText(price);
            String stock = tableBook.getModel().getValueAt(row, 4).toString();
            stockText.setText(stock);

        }

    }

    private void modifyBook() {
        if(this.idText.getText().equals("")) {
            showMessage("You must select a register...");
        } else {
            // Verificar que el nombre del libro no sea nulo
            if(bookText.getText().equals("")) {
                showMessage("Provide the name of the book...");
                bookText.requestFocusInWindow();
                return;
            }
            // LLenar el objeto libro a actualizar
            int idBook = Integer.parseInt(idText.getText());
            var nameBook = bookText.getText();
            var author = authorText.getText();
            var price = Double.parseDouble(priceText.getText());
            var stock = Integer.parseInt(stockText.getText());

            var book = new Book(idBook, nameBook, author, price, stock);
            bookService.saveBook(book);
            showMessage("Book modified... ");
            cleanForm();
            listBooks();
        }
    }

    private void deleteBook() {
        var row = tableBook.getSelectedRow();
        if(row != -1) {
            String idBook = tableBook.getModel().getValueAt(row, 0).toString();
            var book = new Book();
            book.setIdBook(Integer.parseInt(idBook));
            bookService.deleteBook(book);
            showMessage("Libro " + idBook + " deleted");
            cleanForm();
            listBooks();
        } else {
            showMessage("No book has been selected to delete...");
        }
    }

    private void cleanForm() {
        bookText.setText("");
        authorText.setText("");
        priceText.setText("");
        stockText.setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        // Crear el elemento idText oculto
        idText = new JTextField("");
        idText.setVisible(false);

        this.tableModelBooks = new DefaultTableModel(0, 5) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] headers = {"Id", "Book", "Author", "Price", "Stock"};
        tableModelBooks.setColumnIdentifiers(headers);

        // Instanciar el objeto JTable
        this.tableBook = new JTable(tableModelBooks);
        // Evitar que se seleccionen varios registros
        tableBook.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listBooks();

    }

    private void listBooks() {
        // Limpiar la tabla
        tableModelBooks.setRowCount(0);

        // Obtener todos los libros
        var books = bookService.listBooks();
        books.forEach(book -> {
            // Añadir el libro a la tabla
            Object[] rowBook = {
                    book.getIdBook(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getPrice(),
                    book.getStock()
            };
            this.tableModelBooks.addRow(rowBook);
        });
    }
}

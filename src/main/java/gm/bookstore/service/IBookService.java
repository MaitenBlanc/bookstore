package gm.bookstore.service;

import gm.bookstore.model.Book;

import java.util.List;

public interface IBookService {
    public List<Book> listBooks();

    public Book getBookById(Integer idBook);

    public void saveBook(Book book);

    public void deleteBook(Book book);
}

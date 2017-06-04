package cz.klimesova.public_library.data;

import java.util.List;

/**
 * Created by Zdenca on 5/30/2017.
 */
public class Library {
    private List<Book> books;
    private List<Author> authors;

    public Library(List<Book> books, List<Author> authors) {
        this.authors = authors;
        this.books = books;
    }
    public List<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Library{" +
                "books=" + books +
                ", authors=" + authors +
                '}';
    }
}

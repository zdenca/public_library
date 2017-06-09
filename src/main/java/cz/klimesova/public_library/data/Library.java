package cz.klimesova.public_library.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zdenca on 5/30/2017.
 */
public class Library {
    private final List<Book> books = new ArrayList<>();
    private final List<Author> authors = new ArrayList<>();

    public Library(List<Book> books, List<Author> authors) {
        this.authors.addAll(authors);
        this.books.addAll(books);
    }

    public List<Author> getAuthors() {
        return new ArrayList<>(authors);
    }

    public void setBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void setAuthors(List<Author> authors) {
        this.authors.clear();
        this.authors.addAll(authors);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }


    @Override
    public String toString() {
        return "Library{" +
                "books=" + books +
                ", authors=" + authors +
                '}';
    }
}

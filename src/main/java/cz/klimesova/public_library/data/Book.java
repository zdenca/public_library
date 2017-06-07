package cz.klimesova.public_library.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zdenca on 5/29/2017.
 */
public class Book {
    private int id;
    private final List<Author> authors = new ArrayList<>();
    private String title;
    private String isbn;
    private int issueYear;


    public Book(int id, String title, List<Author> authors, String isbn, int issueYear) {
        this.id = id;
        this.title = title;
        this.authors.addAll(authors);
        this.isbn = isbn;
        this.issueYear = issueYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Author> getAuthors() {
        return new ArrayList<>(authors);
    }

    public void setAuthors(List<Author> authors) {
        this.authors.clear();
        this.authors.addAll(authors);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getIssueYear() {
        return issueYear;
    }

    public void setIssueYear(int issueYear) {
        this.issueYear = issueYear;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", authors=" + authors +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", issueYear=" + issueYear +
                '}';
    }
}

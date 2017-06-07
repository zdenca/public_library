package cz.klimesova.public_library.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zdenca on 6/7/2017.
 */
public class LibraryTest {
    @Test
    public void getAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        Author a = new Author(12, new Name("aa", "BB"));
        authors.add(a);
        Library lib = new Library(books, authors);
        authors.clear();
        assertNotNull(lib.getAuthors());
        assertEquals(1, lib.getAuthors().size());
    }

    @Test
    public void setAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        Author a = new Author(12, new Name("aa", "BB"));
        authors.add(a);
        Library lib = new Library(books, authors);
        List<Author> authors2 = new ArrayList<>();
        Author a2 = new Author(45, new Name("he", "llo"));
        authors2.add(a2);
        lib.setAuthors(authors2);
        assertEquals(authors2, lib.getAuthors());
        List<Author> authors3 = new ArrayList<>(authors2);
        authors2.clear();
        assertEquals(1, lib.getAuthors().size());
        assertEquals(authors3, lib.getAuthors());
    }

    @Test
    public void getBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Book b = new Book(3, "Testing book", authors, "ISBN-test", 789);
        books.add(b);
        Library lib = new Library(books, authors);
        books.clear();
        assertNotNull(lib.getBooks());
        assertEquals(1, lib.getBooks().size());
    }

    @Test
    public void setBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Book b = new Book(3, "Testing book", authors, "ISBN-test", 789);
        books.add(b);
        Library lib = new Library(books, authors);
        List<Book> books2 = new ArrayList<>();
        Book b2 = new Book(76, "Another book", authors, "isbn", 324);
        books2.add(b2);
        lib.setBooks(books2);
        assertEquals(books2, lib.getBooks());
        List<Book> books3 = new ArrayList<>(books2);
        books2.clear();
        assertEquals(1, lib.getBooks().size());
        assertEquals(books3, lib.getBooks());
    }


}
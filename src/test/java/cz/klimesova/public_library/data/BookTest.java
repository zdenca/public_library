package cz.klimesova.public_library.data;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zdenca on 6/7/2017.
 */
public class BookTest {
    @Test
    public void getAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1, new Name("Ah", "P")));
        Book book = new Book(23, "Testing book", authors, "ISBN-Test", 1234);
        authors.clear();
        assertNotNull(book.getAuthors());
        assertEquals(1, book.getAuthors().size());
    }

    @Test
    public void setAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(1, new Name("Ah", "P")));
        Book book = new Book(23, "Testing book", authors, "ISBN-Test", 1234);
        List<Author> authors2 = new ArrayList<>();
        authors2.add(new Author(4, new Name("he", "pci")));
        book.setAuthors(authors2);
        assertEquals(authors2, book.getAuthors());
        List<Author> authors3 = new ArrayList<>(authors2);
        authors2.clear();
        assertEquals(1, book.getAuthors().size());
        assertEquals(authors3, book.getAuthors());
    }
}
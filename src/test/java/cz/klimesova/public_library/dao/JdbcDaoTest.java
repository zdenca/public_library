package cz.klimesova.public_library.dao;

import cz.klimesova.public_library.AbstractLibraryTest;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Name;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zdenca on 5/30/2017.
 */
public class JdbcDaoTest extends AbstractLibraryTest {

    @Test
    public void saveBook() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(0, new Name("Karel", "B")));
        authors.add(new Author(0, new Name("Jaroslav", "P")));

        Book b = new Book(0, "Test book again", authors, "ISBN_TEST", 2010);

        dao.saveBook(b);
        for (Author a : b.getAuthors()) {
            assertNotEquals(0, a.getId());
        }

        assertNotEquals(0, b.getId());
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from books");
        if (rs.next()) {
            assertEquals(1, rs.getInt(1));
        }

    }

    @Test
    public void deleteBook() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(0, new Name("Karla", "Zelenina")));
        authors.add(new Author(0, new Name("Jana", "Zelenina")));
        Book b = new Book(1, "Test deleting book1", authors, "ISBN-00", 10);

        dao.saveBook(b);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from books");
        rs.next();
        assertEquals(1, rs.getInt(1));
        ResultSet rsA = stmt.executeQuery("select count(*) from authors");
        rsA.next();
        assertEquals(2, rsA.getInt(1));
        ResultSet rsB2A = stmt.executeQuery("select count(*) from books_to_authors");
        rsB2A.next();
        assertEquals(2, rsB2A.getInt(1));

        dao.deleteBook(b);
        ResultSet rs1 = stmt.executeQuery("select count(*) from books");
        rs1.next();
        assertEquals(0, rs1.getInt(1));
        ResultSet rs2 = stmt.executeQuery("select count(*) from books_to_authors");
        rs2.next();
        assertEquals(0, rs2.getInt(1));

    }

    @Test
    public void updateBook() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(0, new Name("Karla", "Zelenina")));
        authors.add(new Author(0, new Name("Jana", "Zelenina")));
        Book book = new Book(0, "Test deleting complete book", authors, "ISBN_Test", 2007);
        dao.saveBook(book);
        book.setTitle("Quo");
        book.setIsbn("000");
        book.setIssueYear(2104);
        book.getAuthors().clear();
        book.getAuthors().add(new Author(0, new Name("Joe", "K.")));
        dao.updateBook(book);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from books where id = " + book.getId());
        rs.next();
        assertEquals(book.getTitle(), rs.getString(2));
        assertEquals(book.getIsbn(), rs.getString(3));
        assertEquals(book.getIssueYear(), rs.getInt(4));
        ResultSet rs2 = statement.executeQuery("select author_id from books_to_authors where book_id = " + book.getId());
        rs2.next();
        assertEquals(book.getAuthors().get(0).getId(), rs2.getInt(1));
    }


    @Test
    public void loadAllBooks() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        List<Book> books = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            List<Author> authors = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                authors.add(new Author(0, new Name("firstName_" + j, "lastName_" + j)));
            }
            Book book = new Book(0, "title_" + i, authors, "isbn_" + i, i);
            books.add(book);
            dao.saveBook(book);
        }
        Collections.sort(books, Comparator.comparing(Book::getTitle));
        List<Book> loadedBooks = dao.loadAllBooks();
        assertEquals(books.size(), loadedBooks.size());
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            Book loadedBook = loadedBooks.get(i);
            assertEquals(book.getId(), loadedBook.getId());
            assertEquals(book.getTitle(), loadedBook.getTitle());
            assertEquals(book.getIsbn(), loadedBook.getIsbn());
            assertEquals(book.getIssueYear(), loadedBook.getIssueYear());
            List<Author> loadedBookAuthors = loadedBook.getAuthors();
            List<Author> bookAuthors = book.getAuthors();
            Collections.sort(bookAuthors, Comparator.comparing((a) -> (a.getName().getLastName())));
            assertAuthors(bookAuthors, loadedBookAuthors);
        }
    }

    @Test
    public void saveAuthor() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        Author author = new Author(0, new Name("J", "D"));
        dao.saveAuthor(author);
        assertNotEquals(0, author.getId());
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from authors");
        rs.next();
        assertEquals(1, rs.getInt(1));
        ResultSet rsA = stmt.executeQuery("select * from authors");
        rsA.next();
        assertEquals(author.getId(), rsA.getInt(1));
        assertEquals(author.getName().getFirstName(), rsA.getString(2));
        assertEquals(author.getName().getLastName(), rsA.getString(3));
    }

    @Test
    public void deleteAuthor() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        Author author = new Author(0, new Name("A", "W"));
        dao.saveAuthor(author);
        dao.deleteAuthor(author);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from authors");
        rs.next();
        assertEquals(0, rs.getInt(1));
    }

    @Test
    public void updateAuthor() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        Author author = new Author(0, new Name("Greg", "P"));
        dao.saveAuthor(author);
        author.setName(new Name("Ole", "B"));
        dao.updateAuthor(author);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from authors where id = " + author.getId());
        rs.next();
        assertEquals(author.getName().getFirstName(), rs.getString(2));
        assertEquals(author.getName().getLastName(), rs.getString(3));
    }

    @Test
    public void loadAllAuthors() throws Exception {
        JdbcDao dao = new JdbcDao(connectionProperties);
        List<Author> authors = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Author author = new Author(0, new Name("first_name_" + i, "last_name_" + i));
            authors.add(author);
            dao.saveAuthor(author);
        }
        Collections.sort(authors, Comparator.comparing(a -> a.getName().getLastName()));
        List<Author> loadedAuthors = dao.loadAllAuthors();
        assertAuthors(authors, loadedAuthors);
    }

    public void assertAuthors(List<Author> authors, List<Author> loadedAuthors) {
        assertEquals(authors.size(), loadedAuthors.size());
        for (int j = 0; j < authors.size(); j++) {
            Author author = authors.get(j);
            Author loadedAuthor = loadedAuthors.get(j);
            assertEquals(author.getId(), loadedAuthor.getId());
            assertEquals(author.getName().getFirstName(), loadedAuthor.getName().getFirstName());
            assertEquals(author.getName().getLastName(), loadedAuthor.getName().getLastName());
        }
    }

    @Test
    public void existsAuthor() {
        JdbcDao dao = new JdbcDao(connectionProperties);
        Author author = createAuthor(0, "first", "last");
        dao.saveAuthor(author);
        assertNotEquals(0, author.getId());
        boolean exists = dao.existsAuthor(author.getId());
        assertTrue(exists);

        exists = dao.existsAuthor(-20);
        assertFalse(exists);
    }

    @Test
    public void existsBook() {
        JdbcDao dao = new JdbcDao(connectionProperties);
        Book book = createBook(0, "Testing", createListOfAuthors(createAuthor(0, "first", "last")), "isbn-0", 1234);
        dao.saveBook(book);
        assertNotEquals(0, book.getId());
        boolean exists = dao.existsBook(book.getId());
        assertTrue(exists);

        exists = dao.existsBook(-20);
        assertFalse(exists);
    }


}

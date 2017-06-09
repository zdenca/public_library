package cz.klimesova.public_library;

import cz.klimesova.public_library.dao.ConnectionProperties;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Name;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zdenca on 6/7/2017.
 */
public abstract class AbstractLibraryTest {
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "plib_test";
    private static final String DB_PASSWORD = "plib_test";

    protected ConnectionProperties connectionProperties = new ConnectionProperties(DB_CONNECTION, DB_USER, DB_PASSWORD);
    protected Connection connection;

    protected Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create database connection", e);
        }
    }

    protected Book createBook(int bookId, String title, List<Author> authors, String isbn, int issueYear) {
        return new Book(bookId, title, authors, isbn, issueYear);
    }

    protected Author createAuthor(int authorId, String firstName, String lastName) {
        Author author = new Author(authorId, new Name(firstName, lastName));
        return author;
    }

    protected List<Author> createListOfAuthors(Author author) {
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        return authors;
    }

    @Before
    public void setUp() {
        connection = getConnection();
    }

    @After
    public void tearDown() throws SQLException {
        Statement delete = connection.createStatement();
        delete.execute("delete from books_to_authors");
        delete.execute("delete from authors");
        delete.execute("delete from books");
        connection.commit();
        connection.close();
    }
}

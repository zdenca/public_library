package cz.klimesova.public_library.dao;

import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Name;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zdenca on 5/30/2017.
 */
public class JdbcDao {
    private final ConnectionProperties connectionProperties;

    public JdbcDao(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionProperties.getConnection(), connectionProperties.getUser(),
                    connectionProperties.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create database connection", e);
        }
    }

    private int getNextBookId() {
        try (Connection connection = getConnection();
             PreparedStatement seqNextVal = connection.prepareStatement("select book_seq.nextval from dual")) {
            ResultSet resultSet = seqNextVal.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new RuntimeException("Returned empty resultSet for book sequence");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get next book id", e);
        }
    }

    private void bindBookToAuthors(int bookId, int authorId, Connection connection) throws SQLException {
        String insertSQL = "insert into books_to_authors values (?,?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            ps.executeUpdate();
        }
    }

    private Map<Author, Integer> bindAndSaveAuthorsToBook(int bookId, List<Author> authors, Connection connection) throws SQLException {
        Map<Author, Integer> newAuthorToId = new HashMap<>();
        for (Author a : authors) {
            int authorId = a.getId();
            if (authorId == 0) {
                authorId = saveAuthor(a, connection);
                newAuthorToId.put(a, authorId);
            }
            bindBookToAuthors(bookId, authorId, connection);
        }
        return newAuthorToId;
    }


    public void saveBook(Book book) {
        String insert = "insert into Books values (?, ?, ?, ?)";
        try (Connection connection = getConnection()) {
            try (PreparedStatement save = connection.prepareStatement(insert)) {
                int bookId = getNextBookId();
                save.setInt(1, bookId);
                save.setString(2, book.getTitle());
                save.setString(3, book.getIsbn());
                save.setInt(4, book.getIssueYear());
                save.executeUpdate();
                Map<Author, Integer> newAuthorToId = bindAndSaveAuthorsToBook(bookId, book.getAuthors(), connection);
                connection.commit();
                setNewSavedAuthorId(newAuthorToId);
                book.setId(bookId);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot insert new book.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Save of book " + book + " failed.", e);
        }
    }

    private void setNewSavedAuthorId(Map<Author, Integer> newAuthorToId) {
        for (Map.Entry<Author, Integer> e : newAuthorToId.entrySet()) {
            e.getKey().setId(e.getValue());
        }
    }

    public void deleteBook(Book book) {
        String deleteBookSQL = "delete from books where id = ?";
        String deleteBookToAuthorSQL = "delete from books_to_authors where book_id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement deleteBookToAuthor = connection.prepareStatement(deleteBookToAuthorSQL);
                 PreparedStatement deleteBook = connection.prepareStatement(deleteBookSQL)) {
                deleteBookToAuthor.setInt(1, book.getId());
                deleteBookToAuthor.executeUpdate();

                deleteBook.setInt(1, book.getId());
                deleteBook.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot delete a book", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Delete of book " + book + " failed", e);
        }
    }

    public void updateBook(Book book) {
        String updateSQL = "update books set title = ?, isbn = ? , issue_year = ? where id = ?";
        String deleteBookToAuthorSQL = "delete from books_to_authors where book_id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement deleteBookToAuthor = connection.prepareStatement(deleteBookToAuthorSQL);
                 PreparedStatement update = connection.prepareStatement(updateSQL)) {
                update.setString(1, book.getTitle());
                update.setString(2, book.getIsbn());
                update.setInt(3, book.getIssueYear());
                update.setInt(4, book.getId());
                update.executeUpdate();

                deleteBookToAuthor.setInt(1, book.getId());
                deleteBookToAuthor.executeUpdate();

                Map<Author, Integer> authorIntegerMap = bindAndSaveAuthorsToBook(book.getId(), book.getAuthors(), connection);

                connection.commit();

                setNewSavedAuthorId(authorIntegerMap);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot update a book" + book + " ", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Update of book " + book + " failed", e);
        }
    }

    private List<Author> loadAuthorsOfBook(int bookId, Connection connection) {
        String loadSQL = "select a.* from books_to_authors ba" +
                " join authors a on a.id = ba.author_id where book_id = " + bookId;
        List<Author> authors = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(loadSQL);
            while (rs.next()) {
                int authorId = rs.getInt("ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                authors.add(new Author(authorId, new Name(firstName, lastName)));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load authors of book failed", e);
        }
        return authors;
    }


    public List<Book> loadAllBooks() {
        String loadSQL = "select * from books";
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(loadSQL)) {
            while (resultSet.next()) {
                int bookId = resultSet.getInt("ID");
                String title = resultSet.getString("TITLE");
                String isbn = resultSet.getString("ISBN");
                int issueYear = resultSet.getInt("ISSUE_YEAR");
                books.add(new Book(bookId, title, loadAuthorsOfBook(bookId, connection), isbn, issueYear));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load all books failed", e);
        }
        return books;
    }

    public int getNextAuthorId() {
        try (Connection connection = getConnection();
             PreparedStatement seqNextVal = connection.prepareStatement("select author_seq.nextval from dual")) {
            ResultSet rs = seqNextVal.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException("Returned empty resultSet for author sequence");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get next author id", e);

        }
    }

    private int saveAuthor(Author author, Connection connection) {
        String insert = "insert into Authors values(?,?,?)";
        try {
            try (PreparedStatement save = connection.prepareStatement(insert)) {
                int authorId = getNextAuthorId();
                save.setInt(1, authorId);
                save.setString(2, author.getName().getFirstName());
                save.setString(3, author.getName().getLastName());
                save.executeUpdate();
                return authorId;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot insert new author", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Save of author " + author + " failed", e);
        }
    }

    public void saveAuthor(Author author) {
        try (Connection connection = getConnection()) {
            try {
                int authorId = saveAuthor(author, connection);
                connection.commit();
                author.setId(authorId);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("", e);
        }
    }

    public void deleteAuthor(Author author) {
        String deleteSQL = "delete from authors where id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement delete = connection.prepareStatement(deleteSQL)) {
                delete.setInt(1, author.getId());
                delete.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot delete a book", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Delete of author " + author + " failed", e);
        }
    }

    public void updateAuthor(Author author) {
        String updateSQL = "update authors set first_name = ?, last_name = ? where id = ?";
        try (Connection connection = getConnection()) {
            try (PreparedStatement update = connection.prepareStatement(updateSQL)) {

                update.setString(1, author.getName().getFirstName());
                update.setString(2, author.getName().getLastName());
                update.setInt(3, author.getId());
                update.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Cannot update an author", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Update of author " + author + "failed", e);
        }
    }

    public List<Author> loadAllAuthors() {
        String loadSQL = "select * from authors";
        List<Author> authors = new ArrayList<>();
        try (Connection connection = getConnection()) {
            Statement load = connection.createStatement();
            ResultSet rs = load.executeQuery(loadSQL);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String firstName = rs.getString("FIRST_NAME");
                String lastName = rs.getString("LAST_NAME");
                authors.add(new Author(id, new Name(firstName, lastName)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load all authors failed", e);
        }
        return authors;
    }


}

package cz.klimesova.public_library.service;

import cz.klimesova.public_library.AbstractLibraryTest;
import cz.klimesova.public_library.dao.ConnectionProperties;
import cz.klimesova.public_library.dao.JdbcDao;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Library;
import cz.klimesova.public_library.data.Name;
import cz.klimesova.public_library.exception.ValidationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Zdenca on 6/7/2017.
 */
public class LibraryServiceTest extends AbstractLibraryTest {
    private final JdbcDao dao = new JdbcDao(connectionProperties);
    private LibraryService libraryService = new LibraryService(dao);

    @Test(expected = ValidationException.class)
    public void saveBookNotNullBook() throws Exception {
        libraryService.saveBook(null);
    }

    @Test(expected = ValidationException.class)
    public void saveBookNullBookId() throws Exception {
        libraryService.saveBook(new Book(1, "jelito", Arrays.asList(new Author(0, new Name("He", "llo"))), "isbn", 1000));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotNullTitle() throws Exception {
        Author a = new Author(0, new Name("hi", "hi"));
        List<Author> authors = new ArrayList<>();
        authors.add(a);
        libraryService.saveBook(new Book(0, null, authors, "isbn", 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotEmptyTitle() {
        libraryService.saveBook(new Book(1, "", Arrays.asList(new Author(0, new Name("ha", "H"))), "isbn", 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotNullIsbn() {
        libraryService.saveBook(createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), null, 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotEmptyIsbn() {
        libraryService.saveBook(createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "", 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotEmptyListOfAuthors() {
        libraryService.saveBook(createBook(0, "Testing book", new ArrayList<Author>(), "isbn", 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotNullLastNameOfAuthor() {
        libraryService.saveBook(createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", null)), "isbn", 1234));
    }

    @Test(expected = ValidationException.class)
    public void saveBookNotEmptyLastNameOfAuthor() {
        libraryService.saveBook(createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "")), "isbn", 1234));
    }


    @Test(expected = ValidationException.class)
    public void updateBookNotNullBook() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        libraryService.updateBook(null);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotNullBookId() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setId(0);
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotNullTitle() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setTitle(null);
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotEmptyTitle() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setTitle("");
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotNullIsbn() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setIsbn(null);
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotEmptyIsbn() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setIsbn("");
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotEmptyListOfAuthors() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setAuthors(new ArrayList<>());
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotNullLastNameOfAuthors() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        List<Author> authors = createListOfAuthors(createAuthor(0, "first", null));
        book.setAuthors(authors);
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void updateBookNotEmptyLastNameOfAuthors() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        List<Author> authors = createListOfAuthors(createAuthor(0, "first", ""));
        book.setAuthors(authors);
        libraryService.updateBook(book);
    }

    @Test(expected = ValidationException.class)
    public void deleteBookNotNulBookId() {
        Book book = createBook(0, "Testing book", createListOfAuthors(createAuthor(0, "first", "last")), "ISBN", 2034);
        libraryService.saveBook(book);
        book.setId(0);
        libraryService.deleteBook(book);
    }

    @Test(expected = ValidationException.class)
    public void saveAuthorNotNullAuthor() {
        libraryService.saveAuthor(null);
    }

    @Test(expected = ValidationException.class)
    public void saveAuthorNullAuthorId() {
        libraryService.saveAuthor(createAuthor(1, "first", "last"));
    }

    @Test(expected = ValidationException.class)
    public void saveAuthorNotNullLastNameOfAuthor() {
        libraryService.saveAuthor(createAuthor(0, "first", null));
    }

    @Test(expected = ValidationException.class)
    public void saveAuthorNotEmptyLastNameOfAuthor() {
        libraryService.saveAuthor(createAuthor(0, "first", ""));
    }

    @Test(expected = ValidationException.class)
    public void updateAuthorNotNullAuthorId() {
        Author author = createAuthor(0, "first", "last");
        libraryService.saveAuthor(author);
        author.setId(0);
        libraryService.updateAuthor(author);
    }

    @Test(expected = ValidationException.class)
    public void updateAuthorNotNullLastNameOfAuthor() {
        Author author = createAuthor(0, "first", "last");
        libraryService.saveAuthor(author);
        author.setName(new Name("first2", null));
        libraryService.updateAuthor(author);
    }

    @Test(expected = ValidationException.class)
    public void updateAuthorNotEmptyLastNameOfAuthor() {
        Author author = createAuthor(0, "first", "last");
        libraryService.saveAuthor(author);
        author.setName(new Name("first2", ""));
        libraryService.updateAuthor(author);
    }

    @Test(expected = ValidationException.class)
    public void deleteAuthorNotNullAuthorId() {
        Author author = createAuthor(0, "first", "last");
        libraryService.saveAuthor(author);
        author.setId(0);
        libraryService.deleteAuthor(author);
    }

    @Test
    public void saveBook() throws Exception {
        List<Author> authors = new ArrayList<>();
        Author author = new Author(0, new Name("He", "llo"));
        authors.add(author);
        Book book = new Book(0, "testing book", authors, "ISBN- test", 1234);
        libraryService.saveBook(book);
        assertTrue(dao.existsBook(book.getId()));
        assertTrue(libraryService.getLibrary().getBooks().contains(book));
    }

    @Test
    public void updateBook() throws Exception {
        List<Author> authors = new ArrayList<>();
        Author author = new Author(0, new Name("He", "llo"));
        authors.add(author);
        Book book = new Book(0, "testing book", authors, "ISBN- test", 1234);
        libraryService.saveBook(book);
        book.setTitle("updated title");
        book.setIssueYear(1000);
        book.setIsbn("updated isbn");
        book.getAuthors().clear();
        book.getAuthors().add(new Author(0, new Name("add", "author")));
        libraryService.updateBook(book);
        for (Book b : libraryService.getLibrary().getBooks()) {
            assertEquals(book.getIsbn(), b.getIsbn());
        }
    }

    @Test
    public void deleteBook() throws Exception {
        List<Author> authors = createListOfAuthors(createAuthor(0, "first", "last"));
        Book book = createBook(0, "Testing book", authors, "ISBN", 2034);
        libraryService.saveBook(book);
        libraryService.deleteBook(book);
        assertFalse(dao.existsBook(book.getId()));
    }

    @Test
    public void saveAuthor() throws Exception {
        Author author = new Author(0, new Name("He", "llo"));
        libraryService.saveAuthor(author);
        assertTrue(dao.existsAuthor(author.getId()));
    }

    @Test
    public void updateAuthor() throws Exception {
        Author author = new Author(0, new Name("He", "llo"));
        libraryService.saveAuthor(author);
        author.setName(new Name("updateFirst", "updateLast"));
        libraryService.updateAuthor(author);
        for (Author a : libraryService.getLibrary().getAuthors())
            assertEquals(author.getName().getLastName(), a.getName().getLastName());
    }

    @Test
    public void deleteAuthor() throws Exception {
        Author author = new Author(0, new Name("He", "llo"));
        libraryService.saveAuthor(author);
        libraryService.deleteAuthor(author);
        assertFalse(dao.existsAuthor(author.getId()));
    }

    @Test
    public void loadLibrary() throws Exception {
        Author a = createAuthor(0, "first", "last");
        Book b = createBook(0, "load book", createListOfAuthors(a), "isbn", 1234);
        libraryService.saveBook(b);
        libraryService.loadLibrary();
        for (Book book : libraryService.getLibrary().getBooks()) {
            assertEquals(book.getId(), b.getId());
        }
    }

}
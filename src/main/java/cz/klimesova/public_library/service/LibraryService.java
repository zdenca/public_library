package cz.klimesova.public_library.service;

import com.sun.xml.internal.bind.v2.TODO;
import cz.klimesova.public_library.dao.JdbcDao;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Library;
import cz.klimesova.public_library.exception.ValidationException;

import java.util.ArrayList;

/**
 * Created by Zdenca on 6/5/2017.
 */
public class LibraryService {
    private final JdbcDao dao;
    private final Library library;


    public LibraryService(JdbcDao dao) {
        this.dao = dao;
        this.library = new Library(new ArrayList<>(), new ArrayList<>());
    }

    public Library getLibrary() {
        return library;
    }

    public void saveBook(Book book) {
        validateBookForSave(book);
        dao.saveBook(book);
        library.addBook(book);
    }
//TODO dodelat updateBook a updateAuthor pro Library

    public void updateBook(Book book) {
        validateBookForUpdate(book);
        dao.updateBook(book);
    }

    public void deleteBook(Book book) {
        validateBookForDelete(book);
        dao.deleteBook(book);
        library.removeBook(book);
    }

    public void saveAuthor(Author author) {
        validateAuthorForSave(author);
        dao.saveAuthor(author);
        library.addAuthor(author);
    }

    public void updateAuthor(Author author) {
        validateAuthorForUpdate(author);
        dao.updateAuthor(author);

    }

    public void deleteAuthor(Author author) {
        validateAuthorForDelete(author);
        dao.deleteAuthor(author);
        library.removeAuthor(author);
    }

    public Library loadLibrary() {
        library.setBooks(dao.loadAllBooks());
        library.setAuthors(dao.loadAllAuthors());
        return library;
    }

    private void validateBookId(Book book) {
        if (!dao.existsBook(book.getId())) {
            throw new ValidationException("Book with id = " + book.getId() + " does not exists");
        }
    }

    private void validateAuthorId(Author author) {
        if (!dao.existsAuthor(author.getId())) {
            throw new ValidationException("Author with id = " + author.getId() + " does not exists");
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new ValidationException("Cannot save/update book: book is required and must not be null");
        }
        if (book.getTitle() == null) {
            throw new ValidationException("Cannot save/update book: title of book is required and must not be null");
        }
        if (book.getTitle() == "") {
            throw new ValidationException("Cannot save/update book: title of book is required and must not be empty");
        }
        if (book.getIsbn() == null) {
            throw new ValidationException("Cannot save/update book: ISBN of book is required and must not be null");
        }
        if (book.getIsbn() == "") {
            throw new ValidationException("Cannot save/update book: ISBN of book is required and must not be empty");
        }
        if (book.getAuthors().isEmpty()) {
            throw new ValidationException("Cannot save/update book: at least one author is required");
        }
        for (Author a : book.getAuthors()) {
            if (a.getName().getLastName() == null) {
                throw new ValidationException("Cannot save/update book: last name of author is required and must not be null");
            }
            if (a.getName().getLastName() == "") {
                throw new ValidationException("Cannot save/update book: last name of author is required and must not be empty");
            }
            if (a.getId() != 0) {
                validateAuthorId(a);
            }
        }
    }

    private void validateBookForSave(Book book) {
        validateBook(book);
        if (book.getId() != 0) {
            throw new ValidationException("Cannot save book: book id must be 0");
        }

    }

    private void validateBookForUpdate(Book book) {
        validateBook(book);
        if (book.getId() == 0) {
            throw new ValidationException("Cannot update book: book id must not be 0");
        }
        validateBookId(book);
    }

    private void validateBookForDelete(Book book) {
        if (book.getId() == 0) {
            throw new ValidationException("Cannot delete book: book id must not be 0");
        }
        validateBookId(book);
    }


    private void validateAuthor(Author author) {
        if (author == null) {
            throw new ValidationException("Cannot save/update author: author is required and must not be null");
        }
        if (author.getName().getLastName() == null) {
            throw new ValidationException("Cannot save/update author: last name of author is required and must not be null");
        }
        if (author.getName().getLastName() == "") {
            throw new ValidationException("Cannot save/update author: last name of author is required and must not be empty");
        }
        if (author.getId() != 0) {
            validateAuthorId(author);
        }
    }

    private void validateAuthorForSave(Author author) {
        validateAuthor(author);
        if (author.getId() != 0) {
            throw new ValidationException("Cannot save author: author id must be 0");
        }
    }

    private void validateAuthorForUpdate(Author author) {
        validateAuthor(author);
        if (author.getId() == 0) {
            throw new ValidationException("Cannot update author: author id must not be 0");
        }
    }

    private void validateAuthorForDelete(Author author) {
        if (author.getId() == 0) {
            throw new ValidationException("Cannot delete author: author id must be not 0");
        }
        validateAuthorId(author);
    }
}




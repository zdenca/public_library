package cz.klimesova.public_library.application;

import cz.klimesova.public_library.dao.ConnectionProperties;
import cz.klimesova.public_library.dao.JdbcDao;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Library;
import cz.klimesova.public_library.data.Name;
import cz.klimesova.public_library.service.LibraryService;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Zdenca on 6/21/2017.
 */
public class Controller implements Initializable {
    private JdbcDao dao = new JdbcDao(new ConnectionProperties("jdbc:oracle:thin:@localhost:1521:orcl", "plib_test", "plib_test"));
    private Library library;
    private LibraryService libraryService = new LibraryService(dao);
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Author> authors = FXCollections.observableArrayList();

    @FXML
    TableView<Book> bookTable;
    @FXML
    TableColumn<Book, String> titleColumn;
    @FXML
    TableColumn<Book, String> firstNameColumn;
    @FXML
    TableColumn<Book, String> lastNameColumn;
    @FXML
    TableColumn<Book, String> isbnColumn;
    @FXML
    TableColumn<Book, Integer> issueYearColumn;
    @FXML
    TableView<Author> authorTable;
    @FXML
    TableColumn<Author, String> firstNameA;
    @FXML
    TableColumn<Author, String> lastNameA;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCellOfTable();
        bookTable.setItems(getOBooks());
        authorTable.setItems(getOAuthors());
    }

    public void handleBookAddButtonClick(ActionEvent event) throws IOException {
        Parent addNewBook = FXMLLoader.load(getClass().getClassLoader().getResource("addBook.fxml"));
        Scene addNewBookScene = new Scene(addNewBook);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(addNewBookScene);
        stage.show();
    }

    public void handleAuthorAddButtonClick(ActionEvent event) throws IOException {
        Parent addNewAuthor = FXMLLoader.load(getClass().getClassLoader().getResource("addAuthor.fxml"));
        Scene addNewAuthorScene = new Scene(addNewAuthor);
        Stage author_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        author_stage.setScene(addNewAuthorScene);
        author_stage.show();
    }

    private void setCellOfTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//        firstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, List<Author>>, ObservableValue<List<Author>>>() {
//            @Override
//            public ObservableValue<List<Author>> call(TableColumn.CellDataFeatures<Book, List<Author>> param) {
//                return new ObservableValueBase<List<Author>>() {
//                    @Override
//                    public List<Author> getValue() {
//                        return param.getValue().getAuthors();
//                    }
//                };
//            }
//        });
        firstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return param.getValue().getAuthors().get(0).getName().getFirstName();
                    }
                };
            }
        });
        lastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> param) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return param.getValue().getAuthors().get(0).getName().getLastName();
                    }
                };
            }
        });
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        issueYearColumn.setCellValueFactory(new PropertyValueFactory<>("issueYear"));
        firstNameA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Author, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Author, String> param) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return param.getValue().getName().getFirstName();
                    }
                };
            }
        });
        lastNameA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Author, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Author, String> param) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return param.getValue().getName().getLastName();
                    }
                };
            }
        });
    }

    private ObservableList<Book> getOBooks() {
        library = libraryService.loadLibrary();
        for (Book b : library.getBooks()) {
            books.add(b);
        }
        return books;
    }

    private ObservableList<Author> getOAuthors() {
        library = libraryService.loadLibrary();
        for (Author a : library.getAuthors()) {
            authors.add(a);
        }
        return authors;
    }

    public void refreshBookTable() {
        books.clear();
        authors.clear();
        bookTable.setItems(getOBooks());
        authorTable.setItems(getOAuthors());
    }


}

package cz.klimesova.public_library.application;

import cz.klimesova.public_library.dao.ConnectionProperties;
import cz.klimesova.public_library.dao.JdbcDao;
import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Library;
import cz.klimesova.public_library.data.Name;
import cz.klimesova.public_library.service.LibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Zdenca on 6/27/2017.
 */
public class AddBookController implements Initializable {
    private JdbcDao dao = new JdbcDao(new ConnectionProperties("jdbc:oracle:thin:@localhost:1521:orcl", "plib_test", "plib_test"));
    private LibraryService libraryService = new LibraryService(dao);

    @FXML
    Button cancel;
    @FXML
    TextField title;
    @FXML
    TextField authorFirstName;
    @FXML
    TextField authorLastName;
    @FXML
    TextField isbn;
    @FXML
    TextField issueYear;

    public void handleButtonCancelClick(ActionEvent event) throws IOException {
        displayFirst(event);
    }

    private void displayFirst(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("first.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void saveAddedBook() {
        List<Author> authorList = new ArrayList<>();
        authorList.add(new Author(0, new Name(authorFirstName.getText(), authorLastName.getText())));
        Book book = new Book(0, title.getText(), authorList, isbn.getText(), Integer.valueOf(issueYear.getText()));
        libraryService.saveBook(book);

    }

    public void handleButtonSaveClick(ActionEvent event) throws IOException {
        saveAddedBook();
        displayFirst(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

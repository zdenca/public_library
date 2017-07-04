package cz.klimesova.public_library.application;

import cz.klimesova.public_library.data.Author;
import cz.klimesova.public_library.data.Book;
import cz.klimesova.public_library.data.Name;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Created by Zdenca on 6/20/2017.
 */
public class Main extends Application {
   private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("first.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Public Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

       public static void main(String[] args) {
        launch(args);
    }
}

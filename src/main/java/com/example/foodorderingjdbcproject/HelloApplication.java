package com.example.foodorderingjdbcproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 750);
        
        // Set the application icon (Original Chef Icon)
        try {
            stage.getIcons().add(new Image("https://cdn-icons-png.flaticon.com/512/1830/1830839.png"));
        } catch (Exception e) {
            System.out.println("Could not load icon");
        }

        stage.setTitle("Food Ordering App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
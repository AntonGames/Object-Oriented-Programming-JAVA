// Anton Zagzin 4 grupė

package com.example.lab_2_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class index extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(index.class.getResource("first_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Būsto paskolos skaičiuoklė (lab_2)");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
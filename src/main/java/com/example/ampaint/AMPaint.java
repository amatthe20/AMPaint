package com.example.ampaint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class AMPaint extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("paint.fxml"));
        PaintController paint = new PaintController();

        Parent root = fxmlLoader.load();
        ((PaintController) fxmlLoader.getController()).setStage(stage);   // for getting the stage to the controller
        Scene scene = new Scene(root);

        stage.setOnCloseRequest(e-> {   // if user tries to close the window
            e.consume();
            paint.onClose();
            LoggerHandle.getLoggerHandle().writeToLog(false,"The end of the AMPaint session.");
        });

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/other/paint.png")));    // official icon
        stage.setTitle("AMPaint App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
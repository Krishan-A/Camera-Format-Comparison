package CameraFormatComparisonPKG;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
Course: CS422 Advanced Programming
Professor: Vic Berry
Project: Final Project
Date: April 30, 2020
Programmer: Krishan Agarwal
Issues:

Assumptions:

Program Description: Compare different camera formats where a primary camera format size, lens, lens settings, and a
secondary camera format size is entered into the program, then a secondary camera lens and information about that shot
is calculated. The image generated from the information provided by this program should inform the user as to what they
need to do to get the same "look" across two different camera formats
*/

/*
Class:FormatComparison
Description: Creates the GUI, attaches it to the scene, and places the scene on the stage. Sets the title of the stage.
Initializes the JavaFX runtime and begins executing the application.
*/

public class CameraFormatComparison extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CameraFormatComparison.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Camera Format Comparison");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
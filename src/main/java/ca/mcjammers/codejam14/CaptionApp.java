package ca.mcjammers.codejam14;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CaptionApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Caption Input App");

        // Create UI elements
        Label instructionLabel = new Label("Enter your caption:");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextArea captionArea = new TextArea();
        captionArea.setPrefRowCount(4);
        Button submitButton = new Button("Submit");
        Label displayLabel = new Label();
        displayLabel.setStyle("-fx-font-size: 14px;");

        // Set up event handling
        submitButton.setOnAction(e -> {
            String caption = captionArea.getText();
            if (caption.trim().isEmpty()) {
                displayLabel.setText("Please enter a caption.");
            } else {
                displayLabel.setText("Your caption:\n" + caption);
                captionArea.clear();
            }
        });

        // Arrange UI elements in a vertical layout
        VBox vbox = new VBox(10, instructionLabel, captionArea, submitButton, displayLabel);
        vbox.setPadding(new Insets(20));

        // Set up the scene and stage
        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}

package ca.mcjammers.codejam14;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            ObjectMapper mapper = new ObjectMapper();

            String caption = captionArea.getText();
            String content = "";
            String json = "{\n" +
                    "     \"model\": \"gpt-4o-mini\",\n" +
                    "     \"messages\": [{\"role\": \"system\", \"content\": \"You are an expert academic assistant tasked with creating ultra-detailed summaries of lecture transcripts. Your summaries should be so complete and thorough that they serve as a full substitute for attending or watching the lecture. Follow these detailed instructions: - Comprehensive Content Coverage: Transcribe every piece of technical information, example, tip, and detail provided in the lecture. Include all definitions, processes, and methods, ensuring that every point made by the professor is captured in full. Retain and clarify even minor asides, comments, or tips that might aid understanding or reinforce the material. - Detailed Example Expansion: For every example provided in the lecture, give a step-by-step breakdown of how it is used to illustrate the concept. Document every step in calculations, derivations, or reasoning processes, ensuring nothing is omitted. If any examples include diagrams, describe them in detail and explain their relevance. - Tips and Practical Insights: Note every small piece of advice, tip, or trick shared by the professor, even if it seems minor. Highlight these tips separately for easy identification and use in study or application. - Accurate Contextualization: Capture the context of each statement, ensuring that technical terms, references, and ideas are fully explained. Make the summary self-contained by filling in gaps where a lecture might assume prior knowledge.- Organized Structure and Flow: Follow the flow of the lecture, maintaining a clear structure with headings, subheadings, and logical divisions for topics and examples. Present content in a clean, hierarchical format that mirrors the professor’s organization. - Study-Oriented Detailing: At the end of the summary, provide a recap of all critical takeaways, tips, and practical applications. Include a glossary of any technical terms or concepts introduced. Add a list of key questions, exercises, or prompts to aid the student in studying the material further. - Instructional Clarity: Avoid paraphrasing in ways that sacrifice clarity or technical accuracy. Instead, ensure every sentence is an accurate and detailed reflection of the professor’s teaching. Use explanatory language to make technical details as clear and accessible as possible. The result should be an in-depth and meticulously detailed summary that serves as a complete substitute for watching or attending the lecture. Users should be able to rely solely on your summary for mastering the material.\"}, {\"role\": \"user\", \"content\": \""+caption+"\"}],\n" +
                    "     \"temperature\": 0.7\n" +
                    "   }";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer <key>>")
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Default is GET
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                JsonNode responseJson = mapper.readTree(response.body());
                content = responseJson.asText();

                System.out.print(content);

                // Print the response status code and body
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());

            } catch (IOException | InterruptedException err) {
                err.printStackTrace();
            }

            if (caption.trim().isEmpty()) {
                displayLabel.setText("Please enter a caption.");
            } else {
                displayLabel.setText("Your caption:\n" + content);
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

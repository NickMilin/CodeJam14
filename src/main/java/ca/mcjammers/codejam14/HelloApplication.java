package ca.mcjammers.codejam14;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Caption Input App");

        // Create UI elements
        Label instructionLabel = new Label("Enter your Lecture Captions:");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextArea captionArea = new TextArea();
        captionArea.setWrapText(true);
        captionArea.setPrefRowCount(4);
        captionArea.setPrefWidth(100);
        Button submitButton = new Button("Submit");
        Button downloadButton = new Button("Download");



        // Create WebView for LaTeX rendering
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Placeholder for HTML content
        final String[] htmlContent = {""};

        // Set up event handling
        submitButton.setOnAction(e -> {
            ObjectMapper mapper = new ObjectMapper();

            String key = "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";

            if (key.isBlank()) {
                throw new IllegalArgumentException("Missing OpenAI API key");
            }

            String caption = captionArea.getText();
            String content = "";

            ObjectNode root = mapper.createObjectNode();
            ArrayNode messages = mapper.createArrayNode();
            ObjectNode systemMessage = mapper.createObjectNode();
            ObjectNode userMessage = mapper.createObjectNode();

            userMessage.put("role", "user");
            userMessage.put("content", caption);
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are an expert academic assistant tasked with creating ultra-detailed summaries of lecture transcripts. Your summaries should be so complete and thorough that they serve as a full substitute for attending or watching the lecture. Follow these detailed instructions: - Comprehensive Content Coverage: Transcribe every piece of technical information, example, tip, and detail provided in the lecture. Include all definitions, processes, and methods, ensuring that every point made by the professor is captured in full. Retain and clarify even minor asides, comments, or tips that might aid understanding or reinforce the material. - Detailed Example Expansion: For every example provided in the lecture, give a step-by-step breakdown of how it is used to illustrate the concept. Document every step in calculations, derivations, or reasoning processes, ensuring nothing is omitted. If any examples include diagrams, describe them in detail and explain their relevance. - Tips and Practical Insights: Note every small piece of advice, tip, or trick shared by the professor, even if it seems minor. Highlight these tips separately for easy identification and use in study or application. - Accurate Contextualization: Capture the context of each statement, ensuring that technical terms, references, and ideas are fully explained. Make the summary self-contained by filling in gaps where a lecture might assume prior knowledge.- Organized Structure and Flow: Follow the flow of the lecture, maintaining a clear structure with headings, subheadings, and logical divisions for topics and examples. Present content in a clean, hierarchical format that mirrors the professor’s organization. - Study-Oriented Detailing: At the end of the summary, provide a recap of all critical takeaways, tips, and practical applications. Include a glossary of any technical terms or concepts introduced. Add a list of key questions, exercises, or prompts to aid the student in studying the material further. - Instructional Clarity: Avoid paraphrasing in ways that sacrifice clarity or technical accuracy. Instead, ensure every sentence is an accurate and detailed reflection of the professor’s teaching. Use explanatory language to make technical details as clear and accessible as possible. The result should be an in-depth and meticulously detailed summary that serves as a complete substitute for watching or attending the lecture. Users should be able to rely solely on your summary for mastering the material. Generate your answer completely in html. Only and only generate a HTML compatible output. Make your response look nice with fancy html functions. For equations, use the html <code> class. Write symbols without latex notation. Generate the body with no margin");
            messages.add(systemMessage);
            messages.add(userMessage);

            root.put("model", "gpt-4o-mini");
            root.set("messages", messages);
            root.put("temperature", 0.7);


            String json = null;
            try {
                json = mapper.writeValueAsString(root);
                System.out.print(json);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }

            if (json.isEmpty()) {
                throw new IllegalArgumentException("Error with input");
            }


            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + key)
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Default is GET
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Print the response status code and body
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());

                JsonNode responseJson = mapper.readTree(response.body());
                content = responseJson.path("choices").get(0).path("message").path("content").asText();

                System.out.print(content);
            } catch (IOException | InterruptedException err) {
                err.printStackTrace();
            }

            htmlContent[0] = sanitizeHTML(content);
            webEngine.loadContent(htmlContent[0]);
        });

        // Set up event handling for the Download button
        downloadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(primaryStage);

            if (file != null) {
                try {
                    convertToPdf(htmlContent[0], file);
                    System.out.print("Success! PDF saved successfully!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.print("Error: Failed to save PDF: " + ex.getMessage());
                }
            }
        });

        // Arrange UI elements in a vertical layout
        VBox vbox = new VBox(10, instructionLabel, captionArea, submitButton, downloadButton, webView);
        vbox.setPadding(new Insets(20));

        // Set up the scene and stage
        Scene scene = new Scene(vbox, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private String sanitizeHTML(String htmlContent) {
        if (htmlContent == null) {
            return "";
        }
        return htmlContent
                .replace("```html", "")  // Escape backslashes
                .replace("```", "");   // Escape hash
    }

    private void convertToPdf(String html, File file) throws IOException {
        if (html == null || html.isEmpty()) {
            throw new IllegalArgumentException("HTML content is empty");
        }
        HtmlConverter.convertToPdf(html, new FileOutputStream(file));
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
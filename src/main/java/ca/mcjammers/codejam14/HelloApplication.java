package ca.mcjammers.codejam14;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

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


        // Create WebView for LaTeX rendering
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Set up event handling
        submitButton.setOnAction(e -> {
            ObjectMapper mapper = new ObjectMapper();


            String sanitizedContent = sanitizeLaTeXForMathJax("");
            String htmlContent = generateMathJaxHTML("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Summary: Transformation Method for PDF</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Summary: Transformation Method for PDF</h1>\n" +
                    "    \n" +
                    "    <h2>Introduction</h2>\n" +
                    "    <p>In the previous lecture, we discussed how to find functions of random variables using the CDF method. The steps involved finding the CDF of the variable to derive its PDF. This new method, called the <strong>Transformation Method</strong> or <strong>Change of Variable Method</strong>, simplifies the process using information from the distribution method or the CDF method.</p>\n" +
                    "    \n" +
                    "    <h2>Transformation Method</h2>\n" +
                    "    <p>Let <code>X</code> be a continuous random variable with:</p>\n" +
                    "    <ul>\n" +
                    "        <li>CDF: <code>F_X(x)</code></li>\n" +
                    "        <li>PDF: <code>f_X(x)</code></li>\n" +
                    "    </ul>\n" +
                    "    <p>If <code>Y = g(X)</code>, where <code>Y</code> is a function of <code>X</code>, the process to find the PDF of <code>Y</code> depends on whether <code>g</code> is strictly increasing or decreasing.</p>\n" +
                    "    \n" +
                    "    <h3>Case 1: Strictly Increasing Function</h3>\n" +
                    "    <ol>\n" +
                    "        <li>\n" +
                    "            Write the CDF of <code>Y</code> in terms of the CDF of <code>X</code>:\n" +
                    "            <ul>\n" +
                    "                <li><code>F_Y(y) = P(Y ≤ y) = P(g(X) ≤ y) = P(X ≤ g⁻¹(y)) = F_X(g⁻¹(y))</code></li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "        <li>\n" +
                    "            Differentiate both sides to find the PDF:\n" +
                    "            <ul>\n" +
                    "                <li><code>f_Y(y) = f_X(g⁻¹(y)) * |d/dy (g⁻¹(y))|</code></li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "    </ol>\n" +
                    "    \n" +
                    "    <h3>Case 2: Strictly Decreasing Function</h3>\n" +
                    "    <p>If <code>g</code> is a strictly decreasing function, the relationship becomes:</p>\n" +
                    "    <ul>\n" +
                    "        <li><code>f_Y(y) = f_X(g⁻¹(y)) * |-d/dy (g⁻¹(y))|</code></li>\n" +
                    "    </ul>\n" +
                    "    \n" +
                    "    <h3>General Formula</h3>\n" +
                    "    <p>Combining both cases, the PDF of <code>Y</code> is:</p>\n" +
                    "    <p><code>f_Y(y) = f_X(g⁻¹(y)) * |d/dy (g⁻¹(y))|</code></p>\n" +
                    "\n" +
                    "    <h2>Example 1: Y = √X</h2>\n" +
                    "    <p>Given:</p>\n" +
                    "    <ul>\n" +
                    "        <li><code>f_X(x) = 2e⁻²ˣ</code> (PDF of an exponential distribution with β = 1/2).</li>\n" +
                    "        <li><code>Y = √X</code> (strictly increasing function).</li>\n" +
                    "    </ul>\n" +
                    "    <p>Steps:</p>\n" +
                    "    <ol>\n" +
                    "        <li>Find the CDF of <code>Y</code>:\n" +
                    "            <ul>\n" +
                    "                <li><code>F_Y(y) = P(Y ≤ y) = P(√X ≤ y) = P(X ≤ y²) = F_X(y²)</code></li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "        <li>Differentiating to find the PDF:\n" +
                    "            <ul>\n" +
                    "                <li><code>f_Y(y) = f_X(y²) * d/dy(y²)</code></li>\n" +
                    "                <li><code>f_Y(y) = 2e⁻²ʸ² * 2y = 4y * e⁻²ʸ²</code> (for <code>y ≥ 0</code>).</li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "    </ol>\n" +
                    "\n" +
                    "    <h2>Example 2: Z = (X - μ) / σ</h2>\n" +
                    "    <p>Given:</p>\n" +
                    "    <ul>\n" +
                    "        <li><code>X ~ N(μ, σ²)</code> (Normal distribution).</li>\n" +
                    "        <li><code>Z = (X - μ) / σ</code>.</li>\n" +
                    "    </ul>\n" +
                    "    <p>Steps:</p>\n" +
                    "    <ol>\n" +
                    "        <li>Find the CDF of <code>Z</code>:\n" +
                    "            <ul>\n" +
                    "                <li><code>F_Z(z) = P(Z ≤ z) = P((X - μ)/σ ≤ z) = P(X ≤ μ + σz) = F_X(μ + σz)</code></li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "        <li>Differentiating to find the PDF:\n" +
                    "            <ul>\n" +
                    "                <li><code>f_Z(z) = f_X(μ + σz) * d/dz(μ + σz)</code></li>\n" +
                    "                <li><code>f_Z(z) = f_X(μ + σz) * σ</code>.</li>\n" +
                    "                <li>Using <code>f_X(x)</code> for the normal distribution, substitute into the formula.</li>\n" +
                    "            </ul>\n" +
                    "        </li>\n" +
                    "    </ol>\n" +
                    "\n" +
                    "    <h2>Practice Resources</h2>\n" +
                    "    <p>Practice problems and solutions for this topic are available under the \"Final Exam Resources\" section on the course platform. These resources will help reinforce the concepts discussed.</p>\n" +
                    "\n" +
                    "    <h2>Conclusion</h2>\n" +
                    "    <p>The Transformation Method simplifies the process of finding the PDF of a function of a random variable by avoiding integration. It builds upon the principles of the CDF method and is applicable to both increasing and decreasing functions.</p>\n" +
                    "</body>\n" +
                    "</html>\n");
            webEngine.loadContent(htmlContent);
        });

        // Arrange UI elements in a vertical layout
        VBox vbox = new VBox(10, instructionLabel, captionArea, submitButton, webView);
        vbox.setPadding(new Insets(20));

        // Set up the scene and stage
        Scene scene = new Scene(vbox, 1000, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String generateMathJaxHTML(String latexContent) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <script type="text/javascript" async
                        src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/3.2.2/es5/tex-mml-chtml.js">
                    </script>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 20px; }
                        .math { font-size: 20px; }
                    </style>
                </head>
                <body>
                    <h2>Rendered LaTeX</h2>
                    <div class="math">\\[%s\\]</div>
                </body>
                </html>
                """.formatted(latexContent.replace("\n", "<br>"));
    }

    private String sanitizeLaTeXForMathJax(String latexContent) {
        if (latexContent == null) {
            return "";
        }
        return latexContent
                .replace("\\begin{itemize}", "<ul>")
                .replace("\\end{itemize}", "</ul>")
                .replace("\\item", "<li>")  // Replace \item with <li>
                .replace("\n", "</li>\n");  // Close the list item on new lines
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
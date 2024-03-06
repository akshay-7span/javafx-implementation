package webcrawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class WebCrawlerAppUsingHTMLUnit extends Application {
    private WebClient createWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: URL Input and Search Button
        TextField urlInput = new TextField();
        urlInput.setPromptText("Enter URL to crawl");
        Button searchLinksButton = new Button("Search Links");
        Button searchSEOButton = new Button("Search SEO Analysis");
        Button searchHTMLButton = new Button("Search HTML Body");
        ProgressIndicator progressIndicator = new ProgressIndicator();
        HBox searchBox = new HBox(10, new Label("URL:"), urlInput, searchLinksButton, searchSEOButton, searchHTMLButton, progressIndicator);
        searchBox.getStyleClass().add("hbox");
        root.setTop(searchBox);

        // Center: Result TextArea
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setWrapText(true);
        resultTextArea.getStyleClass().add("text-area");
        root.setCenter(resultTextArea);

        // Apply CSS Styling
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        // Disable progress indicator initially
        progressIndicator.setVisible(false);

        // Search Links Button Action
        searchLinksButton.setOnAction(event -> {
            String url = urlInput.getText().trim();
            if (!url.isEmpty()) {
                progressIndicator.setVisible(true); // Show progress indicator
                searchLinksButton.setDisable(true); // Disable buttons during operation
                searchSEOButton.setDisable(true);
                searchHTMLButton.setDisable(true);
                crawlLinks(url, resultTextArea, progressIndicator, searchLinksButton, searchSEOButton, searchHTMLButton);
            }
        });

        // Search SEO Analysis Button Action
        searchSEOButton.setOnAction(event -> {
            String url = urlInput.getText().trim();
            if (!url.isEmpty()) {
                progressIndicator.setVisible(true); // Show progress indicator
                searchLinksButton.setDisable(true); // Disable buttons during operation
                searchSEOButton.setDisable(true);
                searchHTMLButton.setDisable(true);
                crawlSEO(url, resultTextArea, progressIndicator, searchLinksButton, searchSEOButton, searchHTMLButton);
            }
        });

        // Search HTML Body Button Action
        searchHTMLButton.setOnAction(event -> {
            String url = urlInput.getText().trim();
            if (!url.isEmpty()) {
                progressIndicator.setVisible(true); // Show progress indicator
                searchLinksButton.setDisable(true); // Disable buttons during operation
                searchSEOButton.setDisable(true);
                searchHTMLButton.setDisable(true);
                crawlHTML(url, resultTextArea, progressIndicator, searchLinksButton, searchSEOButton, searchHTMLButton);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Web Crawler JavaFX");
        primaryStage.show();
    }

    private void crawlLinks(String url, TextArea resultTextArea, ProgressIndicator progressIndicator, Button searchLinksButton, Button searchSEOButton, Button searchHTMLButton) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (final WebClient webClient = createWebClient()) { // Removed BrowserVersion.CHROME
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute
                    webClient.getOptions().setJavaScriptEnabled(true);
                    webClient.getOptions().setThrowExceptionOnScriptError(false);


                    // Select anchor elements containing hyperlinks
                    java.util.List<HtmlAnchor> links = page.getAnchors();

                    // Update the TextArea with the crawled links
                    Platform.runLater(() -> {
                        resultTextArea.setText(""); // Clear previous content
                        for (HtmlAnchor link : links) {
                            String linkHref = link.getHrefAttribute();
                            String linkText = link.getTextContent();
                            resultTextArea.appendText(linkText + " : " + linkHref + "\n");
                            try {
                                crawlLinks(url, linkHref);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                } catch (IOException e) {
                    // Handle and display any IO exceptions
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: IO exception - " + e.getMessage());
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace(); // Print the stack trace for debugging
                } catch (Exception e) {
                    // Handle and display any other exceptions
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: " + e.getMessage());
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace(); // Print the stack trace for debugging
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void crawlSEO(String url, TextArea resultTextArea, ProgressIndicator progressIndicator, Button searchLinksButton, Button searchSEOButton, Button searchHTMLButton) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (final WebClient webClient = createWebClient()) {
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000);

                    // Extract SEO-related information
                    String title = page.getTitleText();
                    DomNodeList<HtmlElement> metaTags = page.getHead().getElementsByTagName("meta");

                    StringBuilder seoReport = new StringBuilder();
                    seoReport.append("SEO Analysis Report for ").append(url).append(":\n");
                    seoReport.append("Title: ").append(title).append("\n\n");

                    // Extract and analyze meta tags
                    seoReport.append("Meta Tags:\n");
                    for (HtmlElement metaTag : metaTags) {
                        String tagName = metaTag.getAttribute("name");
                        String tagContent = metaTag.getAttribute("content");
                        seoReport.append(tagName).append(": ").append(tagContent).append("\n");
                    }

                    // Extract and analyze header tags
                    seoReport.append("\nHeader Tags :\n");
                    Iterable<HtmlElement> allElements = page.getHtmlElementDescendants();
                    for (DomElement element : allElements) {
                        String tagName = element.getTagName();
                        if (tagName.matches("h[1-6]")) { // Check if the tag name is h1, h2, h3, h4, h5, or h6
                            String tagContent = element.getTextContent();
                            seoReport.append(tagName).append(": ").append(tagContent).append("\n");
                        }
                    }
                    crawlSEO(url, seoReport.toString());

                    Platform.runLater(() -> {
                        resultTextArea.setText(seoReport.toString());
                        progressIndicator.setVisible(false);
                        searchLinksButton.setDisable(false);
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: IO exception - " + e.getMessage());
                        progressIndicator.setVisible(false);
                        searchLinksButton.setDisable(false);
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace();
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: " + e.getMessage());
                        progressIndicator.setVisible(false);
                        searchLinksButton.setDisable(false);
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void crawlHTML(String url, TextArea resultTextArea, ProgressIndicator progressIndicator, Button searchLinksButton, Button searchSEOButton, Button searchHTMLButton) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try (final WebClient webClient = createWebClient()) { // Removed BrowserVersion.CHROME
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute
                    webClient.getOptions().setJavaScriptEnabled(false);
                    webClient.getOptions().setThrowExceptionOnScriptError(false);


                    // Extract HTML body
                    String htmlBody = page.getBody().asXml();
                    crawlHTML(url, htmlBody);
                    // Update the TextArea with the HTML body
                    Platform.runLater(() -> {
                        resultTextArea.setText(htmlBody);
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                } catch (IOException e) {
                    // Handle and display any IO exceptions
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: IO exception - " + e.getMessage());
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace(); // Print the stack trace for debugging
                } catch (Exception e) {
                    // Handle and display any other exceptions
                    Platform.runLater(() -> {
                        resultTextArea.setText("Error: " + e.getMessage());
                        progressIndicator.setVisible(false); // Hide progress indicator
                        searchLinksButton.setDisable(false); // Enable buttons
                        searchSEOButton.setDisable(false);
                        searchHTMLButton.setDisable(false);
                    });
                    e.printStackTrace(); // Print the stack trace for debugging
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void insertData(String url, String link, String seoAnalysis, String htmlBody) throws SQLException {
        // Connect to the database
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root");
        String sql = "INSERT INTO crawled_data (url, link, seo_analysis, html_body) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, url);
                statement.setString(2, link);
                statement.setString(3, seoAnalysis);
                statement.setString(4, htmlBody);
                statement.executeUpdate();
            }
    }
    private void crawlLinks(String url, String link) throws SQLException {
        insertData(url, link, null, null);
    }

    private void crawlSEO(String url, String seoAnalysis) throws SQLException {
        insertData(url, null, seoAnalysis, null);
    }

    private void crawlHTML(String url, String htmlBody) throws SQLException {
        insertData(url, null, null, htmlBody);
    }
    public static void main(String[] args) {
        launch(args);
    }
}



//----------------------------------------------------------------------------------------------
/*
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WebCrawlerAppUsingHTMLUnit extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Web Crawler with HTMLUnit");

        // Create TextField for entering URL
        TextField urlTextField = new TextField();
        urlTextField.setPromptText("Enter URL");

        // Create TextArea to display crawled content
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20); // Set initial rows
        textArea.setPrefColumnCount(60); // Set initial columns

        // Create Button to trigger crawling action
        Button crawlButton = new Button("Crawl Website");
        crawlButton.setOnAction(e -> {
            // Instantiate a WebClient
            try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                // Suppress some warnings to keep the output clean
                java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);

                // Configure WebClient to ignore JavaScript errors
                webClient.getOptions().setThrowExceptionOnScriptError(false);

                // Set up WebClient options
                webClient.getOptions().setJavaScriptEnabled(true); // Enable JavaScript support
                webClient.getOptions().setCssEnabled(true); // Enable CSS support
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false); // Do not throw exception on failing status code

                // Get the URL from the TextField
                String url = urlTextField.getText().trim();

                // Fetch the HTML page
                HtmlPage page = webClient.getPage(url);

                // Extract the page content
                String pageContent = page.asXml(); // You can use asXml() to get the HTML content as a string

                // Display the content in the TextArea
                textArea.setText(pageContent);
            } catch (Exception ex) {
                // Handle exceptions
                ex.printStackTrace();
                textArea.setText("Error: " + ex.getMessage());
            }
        });

        // Create a layout and add components
        VBox root = new VBox();
        root.setSpacing(10); // Set spacing between components
        root.getChildren().addAll(urlTextField, crawlButton, textArea);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 800, 600); // Set initial scene size
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/

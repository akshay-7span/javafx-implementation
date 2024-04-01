package webcrawler.newUI;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static webcrawler.newUI.DatabaseCall.*;

public class WebCrawlingApp extends Application {

    private final ProgressBar progressBar = new ProgressBar();
    private final CountDownLatch latch = new CountDownLatch(3);

    @Override
    public void start(Stage primaryStage) {
        // Setting up the title of the application window
        primaryStage.setTitle("Web Crawling");

        // Creating text area
        TextField urlTextField = new TextField();
        urlTextField.setPromptText("Enter URL"); // Displaying the text as prompt in the text field

        // Setting up the width of the text field
        urlTextField.setPrefWidth(400);
        // Creating button
        Button startButton = new Button("Start Crawling");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");

        Button exportButton = new Button("Export Results");
        exportButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");




        progressBar.setVisible(false);
        progressBar.setStyle("-fx-pref-width: 200px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;"); // Set width, height, and color


        startButton.setOnAction(event -> {
            String url = urlTextField.getText().trim();
            if (!url.isEmpty()) {
                progressBar.setVisible(true);
                disableButtons(startButton);
                startCrawling(url, progressBar);
            }
        });

        exportButton.setOnAction(event -> {
            try {
                exportToExcel();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Creating an HBox to hold the URL text field and button horizontally
        HBox hbox = new HBox(10); // spacing = 10
        hbox.getChildren().addAll(urlTextField, startButton, progressBar,exportButton);
        hbox.setAlignment(Pos.CENTER);


        // Create BackgroundFill for VBox backgrounds
        BackgroundFill vboxBackgroundFill = new BackgroundFill(Color.web("#FAFAFA"), new CornerRadii(10), null);
        Background vboxBackground = new Background(vboxBackgroundFill);

        // Apply CSS to the HBox
        hbox.getStyleClass().add("hbox");

        // Creating VBoxs
        VBox leftTopVBox = createBorderedVBox(1200, 300);
        VBox leftBottomVBox = createBorderedVBox(1200, 300);
        VBox rightTopVBox = createBorderedVBox(300, 550);
        VBox rightBottomVBox = createBorderedVBox(300, 550);

        // Applying background color and effects
        leftTopVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        leftBottomVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightTopVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightBottomVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");

        // Creating a TreeView

        TreeItem<String> rootItem = new TreeItem<>("Crawling Details");

        TreeItem<String> SEO = new TreeItem<>("SEO");
        TreeItem<String> meta = new TreeItem<>("Meta");
        TreeItem<String> h1 = new TreeItem<>("H1");

        TreeItem<String> Links = new TreeItem<>("Link");
        TreeItem<String> status = new TreeItem<>("Status");
        TreeItem<String> pageName = new TreeItem<>("Page name");

        TreeItem<String> Images = new TreeItem<>("Images");
        TreeItem<String> imageSize = new TreeItem<>("Image size");
        TreeItem<String> metaText = new TreeItem<>("Meta text");

        SEO.getChildren().addAll(meta,h1);
        Links.getChildren().addAll(status,pageName);
        Images.getChildren().addAll(imageSize,metaText);

        rootItem.getChildren().addAll(SEO,Links,Images);

        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setPrefSize(300, 550);
        rightTopVBox.getChildren().add(treeView);

        // Create custom cell factory for the TreeView
        treeView.setCellFactory(tree -> new TreeCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // Check if the item is empty or null
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                } else {
                    // Set the text for the cell
                    setText(item);

                    // Check if the item is one of the specified nodes
                    TreeItem<String> treeItem = getTreeItem();
                    if (treeItem == rootItem || treeItem == SEO || treeItem == Links || treeItem == Images) {
                        // Add top and bottom borders
                        setStyle("-fx-border-color: black; -fx-border-width: 1px 0;"); // Top and bottom borders
                    } else {
                        setStyle(null);
                    }
                }
            }
        });


        // Creating the TableView for displaying data
        TableView<SEOData> tableView1 = new TableView<>();
        TableColumn<SEOData, String> pageNameCol1 = new TableColumn<>("Page Name");
        TableColumn<SEOData, String> contentTypeCol1 = new TableColumn<>("Content Type");
        TableColumn<SEOData, String> pageUrlCol1 = new TableColumn<>("Page URL");

        pageNameCol1.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        contentTypeCol1.setCellValueFactory(new PropertyValueFactory<>("contentType"));
        pageUrlCol1.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));

        tableView1.getColumns().addAll(pageNameCol1, contentTypeCol1, pageUrlCol1);

        // Creating another TableView for displaying metaData
        TableView<SEOData> tableView2 = new TableView<>();
        TableColumn<SEOData, String> pageNameCol2 = new TableColumn<>("Page Name");
        TableColumn<SEOData, String> contentTypeCol2 = new TableColumn<>("Content Type");
        TableColumn<SEOData, String> pageUrlCol2 = new TableColumn<>("Page URL");
        TableColumn<SEOData, String> metaCol = new TableColumn<>("Meta");

        pageNameCol2.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        contentTypeCol2.setCellValueFactory(new PropertyValueFactory<>("contentType"));
        pageUrlCol2.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        metaCol.setCellValueFactory(new PropertyValueFactory<>("meta"));


        tableView2.getColumns().addAll(pageNameCol2, contentTypeCol2, pageUrlCol2, metaCol);

        // Creating another TableView for displaying H1
        TableView<SEOData> tableView3 = new TableView<>();
        TableColumn<SEOData, String> pageNameCol3 = new TableColumn<>("Page Name");
        TableColumn<SEOData, String> pageUrlCol3 = new TableColumn<>("Page URL");
        TableColumn<SEOData, String> H1 = new TableColumn<>("H1");

        pageNameCol3.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        pageUrlCol3.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        H1.setCellValueFactory(new PropertyValueFactory<>("H1"));

        tableView3.getColumns().addAll(pageNameCol3,pageUrlCol3,H1);

        TableView<LinkData> tableViewLinks = new TableView<>();
        TableColumn<LinkData, String> pageNameColLinks = new TableColumn<>("Page Name");
        TableColumn<LinkData, String> urlColLinks = new TableColumn<>("URL");
        TableColumn<LinkData, String> statusColLinks = new TableColumn<>("Status");

        pageNameColLinks.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        urlColLinks.setCellValueFactory(new PropertyValueFactory<>("url"));
        statusColLinks.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableViewLinks.getColumns().addAll(pageNameColLinks, urlColLinks, statusColLinks);

        TableView<LinkData> tableViewLinks1 = new TableView<>();
        TableColumn<LinkData, String> pageNameColLinks1 = new TableColumn<>("Page Name");
        TableColumn<LinkData, String> statusColLinks1 = new TableColumn<>("Status");

        pageNameColLinks1.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        statusColLinks1.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableViewLinks1.getColumns().addAll(pageNameColLinks1, statusColLinks1);

        TableView<LinkData> tableViewLinks2 = new TableView<>();
        TableColumn<LinkData, String> pageNameColLinks3 = new TableColumn<>("Page Name");
        TableColumn<LinkData, String> url1 = new TableColumn<>("URL");

        pageNameColLinks3.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        url1.setCellValueFactory(new PropertyValueFactory<>("url"));

        tableViewLinks2.getColumns().addAll(pageNameColLinks3, url1);

        TableView<ImageData> tableViewImages = new TableView<>();
        TableColumn<ImageData, String> pageUrlColImages = new TableColumn<>("Page URL");
        TableColumn<ImageData, String> pageNameColImages = new TableColumn<>("Page Name");
        TableColumn<ImageData, String> imageUrlColImages = new TableColumn<>("Image URL");

        pageUrlColImages.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        pageNameColImages.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        imageUrlColImages.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        tableViewImages.getColumns().addAll(pageUrlColImages, pageNameColImages, imageUrlColImages);

        // Creating another TableView for displaying image data based on image size
        TableView<ImageData> tableViewImageSize = new TableView<>();
        TableColumn<ImageData, String> imageSizeCol = new TableColumn<>("Image Size");
        TableColumn<ImageData, String> imageUrlCol = new TableColumn<>("Image URL");

        imageSizeCol.setCellValueFactory(new PropertyValueFactory<>("imageSize"));
        imageUrlCol.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        tableViewImageSize.getColumns().addAll( imageUrlCol, imageSizeCol);

        // Creating another TableView for displaying image data based on meta text
        TableView<ImageData> tableViewMetaText = new TableView<>();
        TableColumn<ImageData, String> metaTextCol = new TableColumn<>("Alt Text");
        TableColumn<ImageData, String> imageUrlColMeta = new TableColumn<>("Image URL");

        imageUrlColMeta.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        metaTextCol.setCellValueFactory(new PropertyValueFactory<>("altText"));


        tableViewMetaText.getColumns().addAll(imageUrlColMeta, metaTextCol);

        // Enable selection in the TableView
        tableView1.setEditable(true);
        tableView2.setEditable(true);
        tableView3.setEditable(true);
        tableViewLinks.setEditable(true);
        tableViewLinks1.setEditable(true);
        tableViewLinks2.setEditable(true);
        tableViewImages.setEditable(true);
        tableViewImageSize.setEditable(true);
        tableViewMetaText.setEditable(true);

        tableView1.getSelectionModel().setCellSelectionEnabled(true);
        tableView2.getSelectionModel().setCellSelectionEnabled(true);
        tableView3.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks1.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks2.getSelectionModel().setCellSelectionEnabled(true);
        tableViewImages.getSelectionModel().setCellSelectionEnabled(true);
        tableViewImageSize.getSelectionModel().setCellSelectionEnabled(true);
        tableViewMetaText.getSelectionModel().setCellSelectionEnabled(true);

        // Applying drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);

        // Applying CSS to the VBoxes
        leftTopVBox.setBackground(vboxBackground);
        leftTopVBox.setEffect(dropShadow);

        leftBottomVBox.setBackground(vboxBackground);
        leftBottomVBox.setEffect(dropShadow);

        rightTopVBox.setBackground(vboxBackground);
        rightTopVBox.setEffect(dropShadow);

        rightBottomVBox.setBackground(vboxBackground);
        rightBottomVBox.setEffect(dropShadow);

        // Example of applying background color and effects to the HBox
        hbox.setBackground(vboxBackground);
        hbox.setEffect(dropShadow);
        // Creating a GridPane as the root node
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(5);
        root.setVgap(5);
        root.setPadding(new Insets(10));

        // Adding components to the GridPane
        root.add(hbox, 0, 0, 2, 1); // Spanning across two columns
        root.add(leftTopVBox, 0, 1);
        root.add(rightTopVBox, 1, 1);
        root.add(leftBottomVBox, 0, 2);
        root.add(rightBottomVBox, 1, 2);

        // Creating the scene
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        // Displaying the stage
        primaryStage.show();

        // Handle TreeView item selection
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getValue().equals("SEO")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableView1);
                    fetchDataForTableView1(tableView1);
                } else if (newValue.getValue().equals("Meta")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableView2);
                    fetchDataForTableView2(tableView2);
                }else if (newValue.getValue().equals("H1")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableView3);
                    fetchDataForTableView3(tableView3);
                }else if (newValue.getValue().equals("Link")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewLinks);
                    fetchDataForLinksTableView(tableViewLinks);
                }else if (newValue.getValue().equals("Status")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewLinks1);
                    fetchDataForLinksTableViewForStatus(tableViewLinks1);
                }else if (newValue.getValue().equals("Page name")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewLinks2);
                    fetchDataForLinksTableViewForPageName(tableViewLinks2);
                }else if (newValue.getValue().equals("Images")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewImages);
                    fetchDataForImagesTableView(tableViewImages);
                }else if (newValue.getValue().equals("Image size")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewImageSize);
                    fetchDataForImageSizeTableView(tableViewImageSize);
                } else if (newValue.getValue().equals("Meta text")) {
                    leftTopVBox.getChildren().clear();
                    leftTopVBox.getChildren().add(tableViewMetaText);
                    fetchDataForMetaTextTableView(tableViewMetaText);
                }
            }
        });

    }
    private void startCrawling(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    crawlLinks(url, progressBar);
                    crawlSEO(url, progressBar);
                    crawlImages(url, progressBar);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void crawlLinks(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                progressBar.setVisible(true);
                try (final WebClient webClient = createWebClient()) {
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

                    // Retrieve the page title
                    String pageTitle = page.getTitleText(); // Retrieve the page title
                    int statusCode = webClient.getPage(url).getWebResponse().getStatusCode(); // Retrieve the status code

                    List<HtmlAnchor> links = page.getAnchors();

                    Platform.runLater(() -> {
                        for (HtmlAnchor link : links) {
                            String linkHref = link.getHrefAttribute();
                            String linkText = link.getTextContent();
                            // Print the linkHref for debugging
                            System.out.println("Link Href: " + linkHref);

                            // Check if the linkHref starts with a forward slash "/"
                            if (linkHref.startsWith("/")) {
                                try {
                                    // Handle relative URLs by appending them to the base URL
                                    URL basePageUrl = new URL(url);
                                    URL absolutePageUrl = new URL(basePageUrl.getProtocol(), basePageUrl.getHost(), basePageUrl.getPort(), linkHref);

                                    String absolutePageUrlString = absolutePageUrl.toString();

                                    if (!isExcludedDomain(absolutePageUrlString)) {
                                        insertLinkData(absolutePageUrlString, linkText, pageTitle, String.valueOf(statusCode)); // Insert full URL, page title, and status into the database
                                    }

                                    // Access the linked page
                                    HtmlPage linkedPage = link.click();
                                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

                                    // Extract links from the linked page and process them
                                    List<HtmlAnchor> pageLinks = linkedPage.getAnchors();
                                    for (HtmlAnchor pageLink : pageLinks) {
                                        String pageLinkHref = pageLink.getHrefAttribute();
                                        String pageLinkText = pageLink.getTextContent().trim();
                                        if (!pageLinkHref.isEmpty() && isValidUrl(pageLinkHref)) {
                                            if (!isExcludedDomain(pageLinkHref)) {
                                                if (pageLinkHref.startsWith("http")) {
                                                    absolutePageUrl = new URL(pageLinkHref);
                                                } else if (pageLinkHref.startsWith("/")) {
                                                    // Handle relative URLs by appending them to the base URL
                                                    basePageUrl = new URL(absolutePageUrlString);
                                                    absolutePageUrl = new URL(basePageUrl.getProtocol(), basePageUrl.getHost(), basePageUrl.getPort(), pageLinkHref);
                                                } else {
                                                    // Handle other types of URLs (e.g., protocol-relative)
                                                    basePageUrl = new URL(absolutePageUrlString);
                                                    absolutePageUrl = new URL(basePageUrl, pageLinkHref);
                                                }

                                                absolutePageUrlString = absolutePageUrl.toString();
                                                insertLinkData(absolutePageUrlString, pageLinkText, pageTitle, String.valueOf(statusCode)); // Insert full URL, page title, and status into the database
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    // Log the error and continue crawling
                                    System.err.println("Error processing link: " + linkHref);
                                    e.printStackTrace();
                                }
                            } else if (isValidUrl(linkHref)) {
                                // Proceed with the existing logic for valid URLs
                                try {
                                    URL absoluteUrl;
                                    if (linkHref.startsWith("http")) {
                                        absoluteUrl = new URL(linkHref);
                                    } else {
                                        // Handle other types of URLs (e.g., protocol-relative)
                                        URL baseUrl = new URL(url);
                                        absoluteUrl = new URL(baseUrl, linkHref);
                                    }

                                    String absoluteUrlString = absoluteUrl.toString();

                                    if (!isExcludedDomain(absoluteUrlString)) {
                                        insertLinkData(absoluteUrlString, linkText, pageTitle, String.valueOf(statusCode)); // Insert full URL, page title, and status into the database
                                    }

                                    // Access the linked page
                                    HtmlPage linkedPage = link.click();
                                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

                                    // Extract links from the linked page and process them
                                    List<HtmlAnchor> pageLinks = linkedPage.getAnchors();
                                    for (HtmlAnchor pageLink : pageLinks) {
                                        String pageLinkHref = pageLink.getHrefAttribute();
                                        String pageLinkText = pageLink.getTextContent().trim();
                                        if (!pageLinkHref.isEmpty() && isValidUrl(pageLinkHref)) {
                                            if (!isExcludedDomain(pageLinkHref)) {
                                                URL absolutePageUrl;
                                                if (pageLinkHref.startsWith("http")) {
                                                    absolutePageUrl = new URL(pageLinkHref);
                                                } else if (pageLinkHref.startsWith("/")) {
                                                    // Handle relative URLs by appending them to the base URL
                                                    URL basePageUrl = new URL(absoluteUrlString);
                                                    absolutePageUrl = new URL(basePageUrl.getProtocol(), basePageUrl.getHost(), basePageUrl.getPort(), pageLinkHref);
                                                } else {
                                                    // Handle other types of URLs (e.g., protocol-relative)
                                                    URL basePageUrl = new URL(absoluteUrlString);
                                                    absolutePageUrl = new URL(basePageUrl, pageLinkHref);
                                                }

                                                String absolutePageUrlString = absolutePageUrl.toString();
                                                insertLinkData(absolutePageUrlString, pageLinkText, pageTitle, String.valueOf(statusCode)); // Insert full URL, page title, and status into the database
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    // Log the error and continue crawling
                                    System.err.println("Error processing link: " + linkHref);
                                    e.printStackTrace();
                                }

                            } else {
                                // Log invalid URLs
                                System.err.println("Invalid URL: " + linkHref);
                            }
                        }
                        // After the crawling task is completed, call decrementActiveTasks
                        decrementActiveTasks();
                    });
                } catch (IOException e) {
                    // Log the error and continue crawling
                    System.err.println("Error accessing page: " + url);
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    private static final Set<String> excludedDomains = new HashSet<>(Arrays.asList(
            "facebook.com", "twitter.com", "instagram.com", "youtube.com", "github.com",
            "veppar.com", "behance.net", "dribbble.com", "discord.com", "twitter.com",
            "linkedin.com", "pagemaker.io", "materialui.co", "x.com"
    ));

    private boolean isExcludedDomain(String url) {
        try {
            URL parsedUrl = new URL(url);
            String domain = parsedUrl.getHost();
            if (domain != null) {
                return excludedDomains.contains(domain);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getHost() == null) {
                return false;
            }
            uri.toURL();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }


    private synchronized void decrementActiveTasks() {
        latch.countDown(); // Decrease the latch count
        if (latch.getCount() == 0) {
            // Update progress bar visibility on the JavaFX application thread
            Platform.runLater(() -> progressBar.setVisible(false));
        }
    }
    private void crawlSEO(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                progressBar.setVisible(true);
                try (final WebClient webClient = createWebClient()) {
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000);

                    // Analyze main page
                    analyzePage(url, page);

                    // Extract links from the main page
                    List<String> links = extractLinks(page);

                    // Visit and analyze attached links
                    for (String link : links) {
                        // Check if the link is not a telephone link
                        if (!isTelephoneLink(link)) {
                            try {
                                HtmlPage attachedPage = webClient.getPage(link);
                                webClient.waitForBackgroundJavaScript(10000);
                                // Analyze the attached page only if it has content to analyze
                                if (hasContentToAnalyze(attachedPage)) {
                                    analyzePage(link, attachedPage);
                                }

                            } catch (FailingHttpStatusCodeException e) {
                                // Handle 404 Not Found error
                                System.err.println("404 Not Found error for URL: " + link);
                                // You can choose to log the error, skip the link, or handle it in any other way
                            } catch (Exception e) {
                                // Handle other exceptions
                                System.err.println("Error processing URL: " + link + " - " + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    Platform.runLater(() -> progressBar.setVisible(false)); // Update progress bar visibility on the JavaFX application thread
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private boolean hasContentToAnalyze(HtmlPage page) {
        return !page.getTitleText().isEmpty() ||
                !page.getHead().getElementsByTagName("meta").isEmpty() ||
                !page.getByXPath("//h1 ").isEmpty();
    }

    private void analyzePage(String url, HtmlPage page) {
        String title = page.getTitleText();
        DomNodeList<HtmlElement> metaTags = page.getHead().getElementsByTagName("meta");
        String contentType = page.getWebResponse().getContentType(); // Extract content type

        StringBuilder metaTagsBuilder = new StringBuilder();
        for (HtmlElement metaTag : metaTags) {
            String tagName = metaTag.getAttribute("name");
            String tagContent = metaTag.getAttribute("content");
            metaTagsBuilder.append("meta: ").append(tagName).append(": ").append(tagContent).append("\n");
        }

        StringBuilder headerTagsBuilder = new StringBuilder();
        Iterable<HtmlElement> allElements = page.getHtmlElementDescendants();
        for (DomElement element : allElements) {
            String tagName = element.getTagName();
            if (tagName.matches("h[1]")) {
                String tagContent = element.getTextContent();
                headerTagsBuilder.append(tagName).append(": ").append(tagContent).append("\n");
            }
        }
        try {
            insertSEOData(url, title, metaTagsBuilder.toString(), headerTagsBuilder.toString(), contentType); // Pass content type
        } catch (SQLException e) {
            System.err.println("Error saving SEO data to the database: " + e.getMessage());
            // Handle the error as needed
        }
    }
    private final Set<String> visitedUrls = new HashSet<>();

    private void crawlImages(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                progressBar.setVisible(true);
                try (final WebClient webClient = createWebClient()) {
                    HtmlPage page = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

                    List<String> links = extractLinks(page); // Extract links from the main page

                    // Iterate over the links and crawl images
                    for (String link : links) {
                        try {
                            crawlImagesHelper(link, webClient);
                        } catch (FailingHttpStatusCodeException e) {
                            if (e.getStatusCode() == 404) {
                                System.err.println("404 Not Found error for URL: " + link);
                                // Handle 404 error appropriately, such as logging the error or skipping the link
                            } else {
                                throw e; // Re-throw the exception if it's not a 404 error
                            }
                        }
                    }
                    decrementActiveTasks();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void crawlImagesHelper(String url, WebClient webClient) throws IOException {
        // Check if the URL has been visited already
        if (visitedUrls.contains(url)) {
            return;
        }
        // Mark the URL as visited
        visitedUrls.add(url);



        // Get the page content
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

        // Extract images from the current page
        List<HtmlImage> images = page.getByXPath("//img");

        Platform.runLater(() -> {
            for (HtmlImage image : images) {
                String imageUrl = image.getAttribute("src");
                String imageAltText = image.getAttribute("alt");
                String imageName = extractImageName(url); // Extract image name from URL
                try {
                    int imageSize = getImageSize(imageUrl);
                    String imageSizeStr = String.format("%.2f", (double) imageSize / 1024) + " KB"; // Convert size to KB and format as string
                    insertImageData(imageName, url, imageUrl, imageAltText, imageSizeStr);
                } catch (SQLException | IOException e) {
                    System.err.println("Error saving image data to the database: " + e.getMessage());
                    // Handle the error as needed
                }
            }
        });
    }

    private String extractImageName(String imageUrl) {
        // Example: Extract the last segment of the URL path
        try {
            URL urlObj = new URL(imageUrl);
            String path = urlObj.getPath();
            // Extract the last segment of the path as the image name
            String[] segments = path.split("/");
            return segments[segments.length - 1];
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Unknown"; // Return a default image name if extraction fails
        }
    }
    private List<String> extractLinks(HtmlPage page) {
        List<String> links = new ArrayList<>();
        DomNodeList<DomElement> anchorTags = page.getElementsByTagName("a");
        String baseUrl = page.getBaseURI(); // Get the base URL of the page
        for (DomElement anchorTag : anchorTags) {
            String link = anchorTag.getAttribute("href");
            if (!link.isEmpty()) {
                // Check if the link starts with "http://" or "https://"
                if (!link.startsWith("http://") && !link.startsWith("https://")) {
                    // If not, prepend the base URL
                    link = baseUrl + link;
                }
                // Filter out telephone links
                if (!isTelephoneLink(link)) {
                    links.add(link);
                }
            }
        }
        return links;
    }

    private int getImageSize(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        return conn.getContentLength();
    }

    private void exportToExcel() throws SQLException {
        System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.SimpleLogger");

        Workbook workbook = new XSSFWorkbook();

        // Styles for title cells
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Create a sheet
        Sheet sheet = workbook.createSheet("Crawling Details");

        // Title for link data section
        Row linkTitleRow = sheet.createRow(0);
        Cell linkTitleCell = linkTitleRow.createCell(0);
        linkTitleCell.setCellValue("Link Data");
        linkTitleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3)); // Merge cells for title
        int currentRow = 2; // Start from row 2 for link data

        // Populate data from the link_crawled_data table
        List<LinkDataForExcel> linkDataList = retrieveLinkDataFromDatabase();
        currentRow = populateData(sheet, linkDataList, currentRow);

        // Title for SEO analysis section
        Row seoTitleRow = sheet.createRow(currentRow);
        Cell seoTitleCell = seoTitleRow.createCell(0);
        seoTitleCell.setCellValue("SEO Analysis");
        seoTitleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 3)); // Merge cells for title
        currentRow += 2; // Start from next row for SEO analysis

        // Populate data from the seo_analysis table
        List<SeoAnalysisData> seoDataList = retrieveSeoAnalysisDataFromDatabase();
        currentRow = populateData(sheet, seoDataList, currentRow);

        // Title for crawled images section
        Row imagesTitleRow = sheet.createRow(currentRow);
        Cell imagesTitleCell = imagesTitleRow.createCell(0);
        imagesTitleCell.setCellValue("Crawled Images");
        imagesTitleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, 5)); // Merge cells for title
        currentRow += 2; // Start from next row for crawled images

        // Populate data from the crawled_images table
        List<CrawledImageData> imageDataList = retrieveCrawledImageDataFromDatabase();
        currentRow = populateData(sheet, imageDataList, currentRow);

        // Autosize columns
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // Get the path to the user's download folder
        String userHomeFolder = System.getProperty("user.home");
        Path downloadFolderPath = Paths.get(userHomeFolder, "Downloads");

        // Create the download folder if it doesn't exist
        try {
            Files.createDirectories(downloadFolderPath);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }

        // Construct the file path for the Excel file in the download folder
        String filePath = downloadFolderPath.resolve("web_crawling_details.xlsx").toString();

        // Write the workbook content to the Excel file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to populate data into the sheet
    private int populateData(Sheet sheet, List<? extends Object> dataList, int startRow) {
        int currentRow = startRow;

        // Adding header row
        Row headerRow = sheet.createRow(currentRow++);
        if (!dataList.isEmpty()) {
            Object firstData = dataList.get(0);
            if (firstData instanceof LinkDataForExcel) {
                headerRow.createCell(0).setCellValue("URL");
                headerRow.createCell(1).setCellValue("Link");
                headerRow.createCell(2).setCellValue("Page Name");
                headerRow.createCell(3).setCellValue("Status");
            } else if (firstData instanceof SeoAnalysisData) {
                headerRow.createCell(0).setCellValue("URL");
                headerRow.createCell(1).setCellValue("Page Name");
                headerRow.createCell(2).setCellValue("Meta Tags");
                headerRow.createCell(3).setCellValue("Header Tags");
                headerRow.createCell(4).setCellValue("Content Type");
            } else if (firstData instanceof CrawledImageData) {
                headerRow.createCell(0).setCellValue("URL");
                headerRow.createCell(1).setCellValue("Page Name");
                headerRow.createCell(2).setCellValue("Image URL");
                headerRow.createCell(3).setCellValue("Alt Text");
                headerRow.createCell(4).setCellValue("Image Size");
            }
        }

        // Populating data
        for (Object data : dataList) {
            Row row = sheet.createRow(currentRow++);
            // Populate data based on the type
            if (data instanceof LinkDataForExcel linkData) {
                row.createCell(0).setCellValue(linkData.getUrl());
                row.createCell(1).setCellValue(linkData.getLink());
                row.createCell(2).setCellValue(linkData.getPageName());
                row.createCell(3).setCellValue(linkData.getStatus());
            } else if (data instanceof SeoAnalysisData seoData) {
                row.createCell(0).setCellValue(seoData.getUrl());
                row.createCell(1).setCellValue(seoData.getPageName());
                row.createCell(2).setCellValue(seoData.getMetaTags());
                row.createCell(3).setCellValue(seoData.getHeaderTags());
                row.createCell(4).setCellValue(seoData.getContentType());
            } else if (data instanceof CrawledImageData imageData) {
                row.createCell(0).setCellValue(imageData.getUrl());
                row.createCell(1).setCellValue(imageData.getPageName());
                row.createCell(2).setCellValue(imageData.getImageUrl());
                row.createCell(3).setCellValue(imageData.getAltText());
                row.createCell(4).setCellValue(imageData.getImageSize());
            }
        }

        return currentRow;
    }



    private boolean isTelephoneLink(String link) {
        return link.startsWith("tel:");
    }

    private WebClient createWebClient() {
        WebClient webClient = new WebClient(com.gargoylesoftware.htmlunit.BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        return webClient;
    }
    private VBox createBorderedVBox(double width, double height) {
        VBox vbox = new VBox();
        vbox.setPrefSize(width, height);
        vbox.getStyleClass().add("vbox");
        return vbox;
    }
    private void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }




    public static class SEOData {
        private final String pageName;
        private final String contentType;
        private final String pageUrl;
        private final String meta;
        private final String H1;

        public SEOData(String pageName, String contentType, String pageUrl, String meta, String h1) {
            this.pageName = pageName;
            this.contentType = contentType;
            this.pageUrl = pageUrl;
            this.meta = meta;
            H1 = h1;
        }

        public String getPageName() {
            return pageName;
        }

        public String getContentType() {
            return contentType;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getMeta() {
            return meta;
        }
        public String getH1(){
            return H1;
        }
    }
    public static class LinkData {
        private final String pageName;
        private final String url;
        private final String status;

        public LinkData(String pageName, String url, String status) {
            this.pageName = pageName;
            this.url = url;
            this.status = status;
        }

        // Getter methods
        public String getPageName() {
            return pageName;
        }

        public String getUrl() {
            return url;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class ImageData {
        private final String pageUrl;
        private final String pageName;
        private final String imageUrl;
        private final String imageSize;
        private final String altText;

        public ImageData(String pageUrl, String pageName, String imageUrl, String imageSize, String altText) {
            this.pageUrl = pageUrl;
            this.pageName = pageName;
            this.imageUrl = imageUrl;
            this.imageSize = imageSize;
            this.altText = altText;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getPageName() {
            return pageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getImageSize() {
            return imageSize;
        }

        public String getAltText() {
            return altText;
        }
    }

    public static class LinkDataForExcel {
        private final String url;
        private final String link;
        private final String pageName;
        private final String status;

        public LinkDataForExcel(String url, String link, String pageName, String status) {
            this.url = url;
            this.link = link;
            this.pageName = pageName;
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public String getLink() {
            return link;
        }

        public String getPageName() {
            return pageName;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class SeoAnalysisData {
        private final String url;
        private final String pageName;
        private final String metaTags;
        private final String headerTags;
        private final String contentType;

        public SeoAnalysisData(String url, String pageName, String metaTags, String headerTags, String contentType) {
            this.url = url;
            this.pageName = pageName;
            this.metaTags = metaTags;
            this.headerTags = headerTags;
            this.contentType = contentType;
        }

        public String getUrl() {
            return url;
        }

        public String getPageName() {
            return pageName;
        }

        public String getMetaTags() {
            return metaTags;
        }

        public String getHeaderTags() {
            return headerTags;
        }

        public String getContentType() {
            return contentType;
        }
    }


    public static class CrawledImageData {
        private final String url;
        private final String pageName;
        private final String imageUrl;
        private final String altText;
        private final String imageSize;
        // Add more fields as needed for image data

        public CrawledImageData(String url, String pageName, String imageUrl, String altText, String imageSize) {
            this.url = url;
            this.pageName = pageName;
            this.imageUrl = imageUrl;
            this.altText = altText;
            this.imageSize = imageSize;
        }

        public String getUrl() {
            return url;
        }

        public String getPageName() {
            return pageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getAltText() {
            return altText;
        }

        public String getImageSize() {
            return imageSize;
        }

    }

}


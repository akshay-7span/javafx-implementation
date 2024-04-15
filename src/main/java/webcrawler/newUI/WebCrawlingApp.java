package webcrawler.newUI;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static webcrawler.newUI.DatabaseCall.fetchDataForImageSizeTableView;
import static webcrawler.newUI.DatabaseCall.fetchDataForImagesTableView;
import static webcrawler.newUI.DatabaseCall.fetchDataForLinksTableView;
import static webcrawler.newUI.DatabaseCall.fetchDataForLinksTableViewForPageName;
import static webcrawler.newUI.DatabaseCall.fetchDataForLinksTableViewForStatus;
import static webcrawler.newUI.DatabaseCall.fetchDataForMetaTextTableView;
import static webcrawler.newUI.DatabaseCall.fetchDataForPageName;
import static webcrawler.newUI.DatabaseCall.fetchDataForTableView1;
import static webcrawler.newUI.DatabaseCall.fetchDataForTableView2;
import static webcrawler.newUI.DatabaseCall.fetchDataForTableView3;
import static webcrawler.newUI.DatabaseCall.fetchDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.fetchHeaderTagsFromDatabase;
import static webcrawler.newUI.DatabaseCall.fetchImageDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.fetchLoadTimeFromDatabase;
import static webcrawler.newUI.DatabaseCall.fetchMetaDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.fetchRepeatedWordsFromDatabase;
import static webcrawler.newUI.DatabaseCall.getDistinctPageData;
import static webcrawler.newUI.DatabaseCall.insertCrawledData;
import static webcrawler.newUI.DatabaseCall.insertImageData;
import static webcrawler.newUI.DatabaseCall.insertSEOData;
import static webcrawler.newUI.DatabaseCall.retrieveCrawledImageDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.retrieveCrawledSeoDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.retrieveLinkDataFromDatabase;
import static webcrawler.newUI.DatabaseCall.retrieveSeoAnalysisDataFromDatabase;

public class WebCrawlingApp extends Application {

    private final ProgressBar progressBar = new ProgressBar();
    private final CountDownLatch latch = new CountDownLatch(3);

    Button exportButton = new Button();

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

        exportButton = new Button("Export Results");
        exportButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");

        progressBar.setVisible(false);
        progressBar.setStyle("-fx-pref-width: 200px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;"); // Set width, height, and color



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
        VBox leftBottomVBox = createBorderedVBox(800, 300);
        VBox rightTopVBox = createBorderedVBox(300, 550);
        VBox rightBottomVBox = createBorderedVBox(800, 550);

        // Applying background color and effects
        leftTopVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        leftBottomVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightTopVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightBottomVBox.setStyle("-fx-background-color: #F4F4F4; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");

        // Create the TabPane
        TabPane tabPane = new TabPane();
        tabPane.setPrefSize(600, 300);

        // Disable closing of tabs
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Create tabs and their respective content
        Tab contentTypeTab = new Tab("Content Type");
        Tab metaTab = new Tab("Meta");
        Tab h1Tab = new Tab("H1");
        Tab imageTab = new Tab("Image");
        Tab pageLoadTimeTab = new Tab("Page Load Time");
        Tab wordCountTab = new Tab("Repeated Word Count");
        Tab links = new Tab("Crawl Links");

        // Add content to tabs (you can add your specific content here)
        // For demonstration, adding a label with the tab name
        contentTypeTab.setContent(new Label("Content Type Content"));
        metaTab.setContent(new Label("Meta Content"));
        h1Tab.setContent(new Label("H1 Content"));
        imageTab.setContent(new Label("Image Content"));
        pageLoadTimeTab.setContent(new Label("Page Load Time Content"));
        wordCountTab.setContent(new Label("Word Count Content"));
        links.setContent(new Label("Crawl Links Content"));


        // Add tabs to the TabPane
        tabPane.getTabs().addAll(contentTypeTab, metaTab, h1Tab, imageTab, pageLoadTimeTab, wordCountTab,links);


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
        treeView.setCellFactory(tree -> new TreeCell<>() {
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


        // Creating a TableView for displaying SEO data
        TableView<SEODataForCrawling> tableViewSEOCrawlingDetails = new TableView<>();
        TableColumn<SEODataForCrawling, String> pageNameCol = new TableColumn<>("Page Name");
        TableColumn<SEODataForCrawling, String> contentTypeCol = new TableColumn<>("Content Type");
        TableColumn<SEODataForCrawling, String> pageUrlCol = new TableColumn<>("Page URL");

        pageNameCol.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        contentTypeCol.setCellValueFactory(new PropertyValueFactory<>("contentType"));
        pageUrlCol.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));

        tableViewSEOCrawlingDetails.getColumns().addAll(pageNameCol, contentTypeCol, pageUrlCol);

        // Add the TableView to the leftTopVBox
        leftTopVBox.getChildren().add(tableViewSEOCrawlingDetails);


        // Create a TableView to display PageData
        TableView<PageData> tableView = new TableView<>();

        // Create TableColumn instances with PropertyValueFactory
        TableColumn<PageData, String> pageNameLinks = new TableColumn<>("Page Name");

        // Set cell value factories using PropertyValueFactory
        pageNameLinks.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        // Add columns to the TableView
        tableView.getColumns().add(pageNameLinks);

        // Create a TableView for the links
        TableView<PageData> linkTableView = new TableView<>();

        // Define columns for the table
        TableColumn<PageData, String> linkUrlCol = new TableColumn<>("Link URL");
        TableColumn<PageData, String> linkTypeCol = new TableColumn<>("Link Type");
        TableColumn<PageData, Integer> statusCol = new TableColumn<>("Status");

        // Set cell value factories to extract data from PageData objects
        linkUrlCol.setCellValueFactory(new PropertyValueFactory<>("linkUrl"));
        linkTypeCol.setCellValueFactory(new PropertyValueFactory<>("linkType"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add columns to the table
        linkTableView.getColumns().addAll(linkUrlCol, linkTypeCol, statusCol);


        // Creating a TableView for displaying SEO data
        TableView<SEODataForCrawling> tableViewSEOCrawlingDetailss = new TableView<>();
        TableColumn<SEODataForCrawling, String> pageNameCols = new TableColumn<>("Page Name");
        TableColumn<SEODataForCrawling, String> contentTypeCols = new TableColumn<>("Content Type");
        TableColumn<SEODataForCrawling, String> pageUrlCols = new TableColumn<>("Page URL");

        pageNameCols.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        contentTypeCols.setCellValueFactory(new PropertyValueFactory<>("contentType"));
        pageUrlCols.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));

        tableViewSEOCrawlingDetailss.getColumns().addAll(pageNameCols, contentTypeCols, pageUrlCols);

        // Creating a TableView for displaying meta data
        TableView<String> tableViewMeta = new TableView<>();
        TableColumn<String, String> metaTagsColumn = new TableColumn<>("Meta Tags");

        // Set up the column to display meta tags
        metaTagsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        // Add the column to the table
        tableViewMeta.getColumns().add(metaTagsColumn);

        // Set up the table to stretch to fill the available space
        tableViewMeta.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Creating a TableView for displaying H1 (header_tags) data
        TableView<String> tableViewH1 = new TableView<>();
        TableColumn<String, String> headerTagsCol = new TableColumn<>("Header Tags");

        // Configure the column to display header_tags
        headerTagsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Add the column to the table
        tableViewH1.getColumns().add(headerTagsCol);

        // Optionally, set a preferred width for the column
        headerTagsCol.prefWidthProperty().bind(tableViewH1.widthProperty().multiply(0.9));

        // Optionally, enable column resizing
        headerTagsCol.setResizable(true);

        // Creating a TableView for displaying repeated words data
        TableView<String> tableViewRepeatedWords = new TableView<>();
        TableColumn<String, String> repeatedWordsCol = new TableColumn<>("Repeated Words");

        // Configure the column to display repeatedWords
        repeatedWordsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Add the column to the table
        tableViewRepeatedWords.getColumns().add(repeatedWordsCol);

        // Optionally, set a preferred width for the column
        repeatedWordsCol.prefWidthProperty().bind(tableViewRepeatedWords.widthProperty().multiply(0.9));

        // Optionally, enable column resizing
        repeatedWordsCol.setResizable(true);



        // Creating a TableView for displaying load time data
        TableView<String> tableViewLoadTime = new TableView<>();
        TableColumn<String, String> loadTimeCol = new TableColumn<>("Load Time");

        // Configure the column to display loadTime
        loadTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        // Add the column to the table
        tableViewLoadTime.getColumns().add(loadTimeCol);

        // Optionally, set a preferred width for the column
        loadTimeCol.prefWidthProperty().bind(tableViewLoadTime.widthProperty().multiply(0.9));

        // Optionally, enable column resizing
        loadTimeCol.setResizable(true);

        // Create the TableView for displaying image data
        TableView<ImageData> imageTable = new TableView<>();

        // Create columns for the table
        TableColumn<ImageData, String> pageUrl = new TableColumn<>("Page URL");
        TableColumn<ImageData, String> pageNameForImage = new TableColumn<>("Page Name");
        TableColumn<ImageData, String> imageUrl = new TableColumn<>("Image URL");
        TableColumn<ImageData, String> imageSizes = new TableColumn<>("Image Size");
        TableColumn<ImageData, String> altTextCol = new TableColumn<>("Alt Text");

        // Set cell value factories to extract data from ImageData objects
        pageUrl.setCellValueFactory(new PropertyValueFactory<>("pageUrl"));
        pageNameForImage.setCellValueFactory(new PropertyValueFactory<>("pageName"));
        imageUrl.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageSizes.setCellValueFactory(new PropertyValueFactory<>("imageSize"));
        altTextCol.setCellValueFactory(new PropertyValueFactory<>("altText"));

        // Add columns to the table
        imageTable.getColumns().addAll(pageUrl, pageNameForImage, imageUrl, imageSizes, altTextCol);


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
        tableView.setEditable(true);

        tableView1.getSelectionModel().setCellSelectionEnabled(true);
        tableView2.getSelectionModel().setCellSelectionEnabled(true);
        tableView3.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks1.getSelectionModel().setCellSelectionEnabled(true);
        tableViewLinks2.getSelectionModel().setCellSelectionEnabled(true);
        tableViewImages.getSelectionModel().setCellSelectionEnabled(true);
        tableViewImageSize.getSelectionModel().setCellSelectionEnabled(true);
        tableViewMetaText.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        // Applying drop shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);

        tabPane.setEffect(dropShadow);

        // Add the TabPane to the leftBottomVBox
        leftBottomVBox.getChildren().add(tabPane);

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


        // Set TabPane to fill height and width
        tabPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tabPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        tabPane.setMaxHeight(Double.MAX_VALUE);
        tabPane.setMaxWidth(Double.MAX_VALUE);

        // Creating the scene
        Scene scene = new Scene(root, 1000, 700);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        // Displaying the stage
        primaryStage.show();

        // Start a background thread to continuously fetch data from the database
        startButton.setOnAction(event -> {
            String url = urlTextField.getText().trim();
            if (!url.isEmpty()) {
                progressBar.setVisible(true);
                disableButtons(startButton);
                startCrawling(url, progressBar);

                // Start a background thread to continuously fetch data from the database
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    try {
                        List<SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                        Platform.runLater(() ->{
                            tableViewSEOCrawlingDetails.getItems().clear();
                            tableViewSEOCrawlingDetails.getItems().addAll(crawledSeoData);
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, 0, 5, TimeUnit.SECONDS);
            }
        });

        // Start a background thread to continuously fetch data from the database
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                List<SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                Platform.runLater(() ->{
                    tableViewSEOCrawlingDetails.getItems().clear();
                    tableViewSEOCrawlingDetails.getItems().addAll(crawledSeoData);
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);

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

        if (!progressBar.isVisible()) {
            // Event handler for URL column click
            tableViewSEOCrawlingDetails.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !tableViewSEOCrawlingDetails.getSelectionModel().isEmpty()) {
                    // Get the selected item
                    SEODataForCrawling selectedItem = tableViewSEOCrawlingDetails.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String selectedUrl = selectedItem.getPageUrl();
                        try {
                            // Fetch data from seo_analysis table based on the selected URL
                            List<SEODataForCrawling> seoDataForCrawlings = fetchDataFromDatabase(selectedUrl);
                            // Clear the existing items in the SEO table
                            tableViewSEOCrawlingDetailss.getItems().clear();
                            // Add the fetched data to the SEO table
                            tableViewSEOCrawlingDetailss.getItems().addAll(seoDataForCrawlings);
                            // Set the content of the SEO tab to the SEO table
                            contentTypeTab.setContent(tableViewSEOCrawlingDetailss);

                            // Fetch meta data based on the selected URL
                            List<String> metaDataList = fetchMetaDataFromDatabase(selectedUrl);
                            // Clear the existing items in the meta table
                            tableViewMeta.getItems().clear();
                            // Add the fetched data to the meta table
                            tableViewMeta.getItems().addAll(metaDataList);
                            // Set the content of the meta tab to the meta table
                            metaTab.setContent(tableViewMeta);

                            // Fetch H1 (header_tags) data from seo_analysis table based on the selected URL
                            List<String> headerTagsList = fetchHeaderTagsFromDatabase(selectedUrl);
                            // Clear the existing items in the H1 (header_tags) table
                            tableViewH1.getItems().clear();
                            // Add the fetched H1 (header_tags) data to the H1 (header_tags) table
                            tableViewH1.getItems().addAll(headerTagsList);
                            // Set the content of the H1 (header_tags) tab to the H1 (header_tags) table
                            h1Tab.setContent(tableViewH1);

                            // Fetch repeated words data from seo_analysis table based on the selected URL
                            List<String> repeatedWordsList = fetchRepeatedWordsFromDatabase(selectedUrl);
                            // Clear the existing items in the repeated words table
                            tableViewRepeatedWords.getItems().clear();
                            // Add the fetched repeated words data to the repeated words table
                            tableViewRepeatedWords.getItems().addAll(repeatedWordsList);
                            // Set the content of the repeated words tab to the repeated words table
                            wordCountTab.setContent(tableViewRepeatedWords);

                            // Fetch load time data from seo_analysis table based on the selected URL
                            List<String> loadTimeList = fetchLoadTimeFromDatabase(selectedUrl);
                            // Clear the existing items in the load time table
                            tableViewLoadTime.getItems().clear();
                            // Add the fetched load time data to the load time table
                            tableViewLoadTime.getItems().addAll(loadTimeList);
                            // Set the content of the load time tab to the load time table
                            pageLoadTimeTab.setContent(tableViewLoadTime);

                            List<ImageData> imageDataList = fetchImageDataFromDatabase(selectedUrl);
                            imageTable.getItems().clear(); // Clear existing items
                            imageTable.getItems().addAll(imageDataList); // Add fetched data to the table

                            imageTab.setContent(imageTable);

                            String url = selectedItem.getPageUrl();
                            VBox vBox = displayGraph(url);
                            rightBottomVBox.getChildren().clear();
                            rightBottomVBox.getChildren().add(vBox);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        // Add a listener to the selection model property of the TabPane
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().equals("Crawl Links")) {
                if (!progressBar.isVisible()) { // Check if progressBar is not visible
                    // Fetching distinct page data from the database
                    List<PageData> distinctPageData = getDistinctPageData();

                    if (!linkTableView.getItems().isEmpty()){
                        linkTableView.getItems().clear();
                    }
                    // Clear existing items in the tableViewLinks
                    tableView.getItems().clear();

                    // Adding fetched data to the tableViewLinks
                    for (PageData pageData : distinctPageData) {
                        PageData data = new PageData(pageData.getPageName());
                        tableView.getItems().add(data);
                    }

                    // Set the content of the "Links" tab to the tableViewLinks
                    links.setContent(tableView);
                }
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage != null) {
                String selectedPageName = newPage.getPageName();
                try {
                    // Query the database using the selected page name
                    List<PageData> pageDataList = fetchDataForPageName(selectedPageName);

                    // Clear existing items in the linkTableView
                    linkTableView.getItems().clear();

                    // Add fetched data to the linkTableView
                    linkTableView.getItems().addAll(pageDataList);
                    links.setContent(linkTableView);
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle database query errors
                }
            }
        });


/*        tableViewSEOCrawlingDetails.setOnMouseClicked(event -> {
            if (!tableViewSEOCrawlingDetails.getSelectionModel().isEmpty() && event.getClickCount() == 1) {
                SEODataForCrawling selectedData = tableViewSEOCrawlingDetails.getSelectionModel().getSelectedItem();
                String url = selectedData.getPageUrl();
                VBox vBox = displayGraph(url);
                rightBottomVBox.getChildren().clear();
                rightBottomVBox.getChildren().add(vBox);
            }
        });*/

    }
    private void startCrawling(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    crawlSEO(url, progressBar);
                    crawlLinks(url, progressBar);
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

   /* private void crawlLinks(String url, ProgressBar progressBar) {
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
*/

    public void crawlLinks(String url, ProgressBar progressBar) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                progressBar.setVisible(true);
                List<String> crawledUrls = new ArrayList<>();
                String domain = extractDomain(url);
                crawlLinksRecursive(url, domain, crawledUrls);
                decrementActiveTasks();
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    private static void crawlLinksRecursive(String url, String domain, List<String> crawledUrls) {
        if (!crawledUrls.contains(url)) {
            crawledUrls.add(url);
            try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setJavaScriptEnabled(false);

                final HtmlPage page = webClient.getPage(url);
                final List<HtmlAnchor> anchors = page.getAnchors();

                for (HtmlAnchor anchor : anchors) {
                    try {
                        String href = anchor.getHrefAttribute();
                        String absoluteUrl = getAbsoluteUrl(url, href);

                        if (!crawledUrls.contains(absoluteUrl)) {
                            int status = getLinkStatus(absoluteUrl);
                            String pageName = page.getTitleText();
                            if (isSameDomain(absoluteUrl, domain)) {
                                insertCrawledData(pageName, url, absoluteUrl, "Internal Link", status);
                                crawlLinksRecursive(absoluteUrl, domain, crawledUrls);
                            } else {
                                insertCrawledData(pageName, url, absoluteUrl, "External Link", status);
                            }
                        }
                    } catch (FailingHttpStatusCodeException e) {
                        // Log the exception or handle it as per your requirement
                        System.err.println("Failed to fetch URL: " + e.getStatusMessage()+ ", Status Code: " + e.getStatusCode());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static String extractDomain(String url) {
        try {
            URL u = new URL(url);
            return u.getHost();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static boolean isSameDomain(String url, String domain) {
        return url.contains(domain);
    }

    private static String getAbsoluteUrl(String baseUrl, String relativeUrl) {
        try {
            URL base = new URL(baseUrl);
            URL absolute = new URL(base, relativeUrl);
            return absolute.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static int getLinkStatus(String link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int status = connection.getResponseCode();
            connection.disconnect();
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // Return an invalid status code for connection errors
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
            // Fetch page load time
            String pageLoadTime = getPageLoadTime(url);

            // Count repeated words
            String repeatedWords = countRepeatedWords(url);
            insertSEOData(url, title, metaTagsBuilder.toString(), headerTagsBuilder.toString(), contentType,pageLoadTime,repeatedWords); // Pass content type
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
                    HtmlPage mainPage = webClient.getPage(url);
                    webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

                    // Crawl images from the main page
                    crawlImagesHelper(url, mainPage, webClient);

                    // Extract links from the main page
                    List<String> links = extractLinks(mainPage);

                    // Iterate over the links and crawl images from linked pages
                    for (String link : links) {
                        try {
                            HtmlPage linkedPage = webClient.getPage(link); // Fetch the linked page
                            webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute on the linked page
                            crawlImagesHelper(linkedPage.getUrl().toString(), linkedPage, webClient); // Crawl images from the linked page
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

    private void crawlImagesHelper(String pageUrl, HtmlPage page, WebClient webClient) {
        // Check if the URL has been visited already
        if (visitedUrls.contains(pageUrl)) {
            return;
        }
        // Mark the URL as visited
        visitedUrls.add(pageUrl);

        // Extract images from the current page
        List<HtmlImage> images = page.getByXPath("//img");
        webClient.waitForBackgroundJavaScript(10000); // Wait for JavaScript to execute

        Platform.runLater(() -> {
            for (HtmlImage image : images) {
                String imageUrl = image.getAttribute("src");
                String imageAltText = image.getAttribute("alt");
                String pageName = extractPageName(pageUrl); // Extract page name from URL
                try {
                    int imageSize = getImageSize(imageUrl);
                    String imageSizeStr = String.format("%.2f", (double) imageSize / 1024) + " KB"; // Convert size to KB and format as string
                    insertImageData(pageName, pageUrl, imageUrl, imageAltText, imageSizeStr);
                } catch (SQLException | IOException e) {
                    System.err.println("Error saving image data to the database: " + e.getMessage());
                    // Handle the error as needed
                }
            }
        });
    }

    private String extractPageName(String pageUrl) {
        try {
            URL urlObj = new URL(pageUrl);
            String path = urlObj.getPath();
            // Remove any leading and trailing slashes
            path = path.replaceAll("^/|/$", "");
            // Extract the last segment of the path as the page name
            String[] segments = path.split("/");
            String pageName = segments[segments.length - 1];
            // Remove file extension, if any
            int dotIndex = pageName.lastIndexOf('.');
            if (dotIndex != -1) {
                pageName = pageName.substring(0, dotIndex);
            }
            return pageName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Unknown"; // Return a default page name if extraction fails
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

    private boolean isTelephoneLink(String link) {
        return link.startsWith("tel:");
    }

    private void exportToExcel() throws SQLException {
        System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.SimpleLogger");

        Workbook workbook = new XSSFWorkbook();

        // Export Link Data
        Sheet linkSheet = workbook.createSheet("Link Data");
        int linkStartRow = createTitleRow(linkSheet, "Link Data", LinkDataForExcel.class);
        List<LinkDataForExcel> linkDataList = retrieveLinkDataFromDatabase();
        populateData(linkSheet, linkDataList, linkStartRow);

        // Export SEO Analysis
        Sheet seoSheet = workbook.createSheet("SEO Analysis");
        int seoStartRow = createTitleRow(seoSheet, "SEO Analysis", SeoAnalysisData.class);
        List<SeoAnalysisData> seoDataList = retrieveSeoAnalysisDataFromDatabase();
        populateData(seoSheet, seoDataList, seoStartRow);

        // Export Crawled Images
        Sheet imageSheet = workbook.createSheet("Crawled Images");
        int imageStartRow = createTitleRow(imageSheet, "Crawled Images", CrawledImageData.class);
        List<CrawledImageData> imageDataList = retrieveCrawledImageDataFromDatabase();
        populateData(imageSheet, imageDataList, imageStartRow);


        // Autosize columns
        for (Sheet sheet : workbook) {
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Show save file dialog
        File selectedFile = showSaveFileDialog();
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();

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
    }

    // Add this method to your class
    private File showSaveFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.setInitialFileName("web_crawling_details.xlsx");

        // Set the default directory
        String userHomeFolder = System.getProperty("user.home");
        File defaultDirectory = new File(userHomeFolder + "/Downloads");
        fileChooser.setInitialDirectory(defaultDirectory);

        // Set the file extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        return fileChooser.showSaveDialog(((Node) exportButton).getScene().getWindow());
    }

    private int createTitleRow(Sheet sheet, String title, Class<?> dataType) {
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        String[] headers;
        if (dataType == LinkDataForExcel.class) {
            // If the data type is LinkDataForExcel, include the additional "Link Type" column
            headers = new String[]{"Page Name", "Page Url", "Link", "Link Type", "Status"};
        } else if (dataType == SeoAnalysisData.class) {
            headers = new String[]{"URL", "Page Name", "Meta Tags", "Header Tags", "Content Type", "Load Time", "Repeated Words"};
        } else if (dataType == CrawledImageData.class) {
            headers = new String[]{"URL", "Page Name", "Image URL", "Alt Text", "Image Size"};
        } else {
            headers = new String[]{};
        }

        Row headerRow = sheet.createRow(1); // Row for headers
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        // Merge cells for title
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        return 2; // Start from row 2 for data
    }

    private void populateData(Sheet sheet, List<? extends Object> dataList, int startRow) {
        int currentRow = startRow;
        for (Object data : dataList) {
            Row row = sheet.createRow(currentRow++);
            // Populate data based on the type
            if (data instanceof LinkDataForExcel linkData) {
                row.createCell(0).setCellValue(linkData.getPageName());
                row.createCell(1).setCellValue(linkData.getPageUrl());
                row.createCell(2).setCellValue(linkData.getLinkUrl());
                row.createCell(3).setCellValue(linkData.getLinkType());
                row.createCell(4).setCellValue(linkData.getStatus());
            } else if (data instanceof SeoAnalysisData seoData) {
                row.createCell(0).setCellValue(seoData.getUrl());
                row.createCell(1).setCellValue(seoData.getPageName());
                row.createCell(2).setCellValue(seoData.getMetaTags());
                row.createCell(3).setCellValue(seoData.getHeaderTags());
                row.createCell(4).setCellValue(seoData.getContentType());
                row.createCell(5).setCellValue(seoData.getLoadTime());
                row.createCell(6).setCellValue(seoData.getRepeatedWords());
            } else if (data instanceof CrawledImageData imageData) {
                row.createCell(0).setCellValue(imageData.getUrl());
                row.createCell(1).setCellValue(imageData.getPageName());
                row.createCell(2).setCellValue(imageData.getImageUrl());
                row.createCell(3).setCellValue(imageData.getAltText());
                row.createCell(4).setCellValue(imageData.getImageSize());
            }
        }
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
    private String getPageLoadTime(String url) {
        // Start time
        long startTime = System.currentTimeMillis();

        // Fetch the webpage content using Jsoup
        try {
            Document doc = Jsoup.connect(url).get();

            // Stop time
            long endTime = System.currentTimeMillis();

            // Calculate page load time
            return String.valueOf(endTime - startTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "unable to fetch page load time"; // Return -1 if unable to fetch page load time
    }

    public static String countRepeatedWords(String url) {
        Map<String, Integer> wordCount = new HashMap<>();
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text(); // Extract text from the body of the HTML document

            // Split the text into words using whitespace as delimiter
            String[] words = text.split("\\s+");

            // Count occurrences of each word
            for (String word : words) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }

            // Filter out non-repeated words
            Map<String, Integer> repeatedWords = new HashMap<>();
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                if (entry.getValue() > 1) {
                    repeatedWords.put(entry.getKey(), entry.getValue());
                }
            }
            return mapToString(repeatedWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to convert map to string
    private static String mapToString(Map<String, Integer> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            stringBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }

    private VBox displayGraph(String pageUrl) {
        // Fetch webpage content and analyze it
        WebPageData data = analyzeWebPage(pageUrl);

        // Create PieChart
        assert data != null;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("HTML", data.getHtmlPercentage()),
                new PieChart.Data("CSS", data.getCssPercentage()),
                new PieChart.Data("JavaScript", data.getJsPercentage()));

        // Set names for the slices with percentages
        for (PieChart.Data slice : pieChartData) {
            String nameWithPercentage = slice.getName() + " (" + slice.getPieValue() + "%)";
            slice.setName(nameWithPercentage);
        }

        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Content Breakdown");

        // Hide slice percentages
        chart.setLabelsVisible(false);

        VBox.setVgrow(chart, Priority.ALWAYS);

        return new VBox(chart);
    }


    private WebPageData analyzeWebPage(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements cssElements = doc.select("link[rel=stylesheet]");
            Elements jsElements = doc.select("script[src]");
            Elements htmlElements = doc.select("html *");

            int totalElements = cssElements.size() + jsElements.size() + htmlElements.size();
            int cssCount = cssElements.size();
            int jsCount = jsElements.size();

            // Calculate percentages
            int cssPercentage = (int) Math.ceil((cssCount * 100.0) / totalElements);
            int jsPercentage = (int) Math.ceil((jsCount * 100.0) / totalElements);
            int htmlPercentage = 100 - cssPercentage - jsPercentage;

            // Adjust CSS percentage to ensure total is 100%
            cssPercentage = 100 - jsPercentage - htmlPercentage;

            return new WebPageData(url, cssPercentage, jsPercentage, htmlPercentage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class WebPageData {
        private String url;
        private int cssPercentage;
        private int jsPercentage;
        private int htmlPercentage;

        public WebPageData(String url, int cssPercentage, int jsPercentage, int htmlPercentage) {
            this.url = url;
            this.cssPercentage = cssPercentage;
            this.jsPercentage = jsPercentage;
            this.htmlPercentage = htmlPercentage;
        }

        public int getCssPercentage() {
            return cssPercentage;
        }

        public int getJsPercentage() {
            return jsPercentage;
        }

        public int getHtmlPercentage() {
            return htmlPercentage;
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
        private String pageName;
        private String pageUrl;
        private String linkUrl;
        private String linkType;
        private int status;

        public LinkDataForExcel(String pageName, String pageUrl, String linkUrl, String linkType, int status) {
            this.pageName = pageName;
            this.pageUrl = pageUrl;
            this.linkUrl = linkUrl;
            this.linkType = linkType;
            this.status = status;
        }

        public String getPageName() {
            return pageName;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public String getLinkType() {
            return linkType;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class SeoAnalysisData {
        private String url;
        private String pageName;
        private String metaTags;
        private String headerTags;
        private String contentType;
        private String loadTime;
        private String repeatedWords;

        public SeoAnalysisData(String url, String pageName, String metaTags, String headerTags, String contentType, String loadTime, String repeatedWords) {
            this.url = url;
            this.pageName = pageName;
            this.metaTags = metaTags;
            this.headerTags = headerTags;
            this.contentType = contentType;
            this.loadTime = loadTime;
            this.repeatedWords = repeatedWords;
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

        public String getLoadTime() {
            return loadTime;
        }

        public String getRepeatedWords() {
            return repeatedWords;
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

    public static class SEODataForCrawling {
        private final String pageName;
        private final String contentType;
        private final String pageUrl;

        public SEODataForCrawling(String pageName, String contentType, String pageUrl) {
            this.pageName = pageName;
            this.contentType = contentType;
            this.pageUrl = pageUrl;
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
    }

    public static class PageData {
        private String pageName;
        private int status;
        private String linkUrl;
        private String linkType;

        public PageData(String pageName, int status, String linkUrl, String linkType) {
            this.pageName = pageName;
            this.status = status;
            this.linkUrl = linkUrl;
            this.linkType = linkType;

        }

        public PageData(String pageName) {
            this.pageName = pageName;
        }

        // Getter methods for the properties
        public String getPageName() {
            return pageName;
        }

        public int getStatus() {
            return status;
        }

        public String getLinkUrl() {
            return linkUrl;
        }
        public String getLinkType() {
            return linkType;
        }
    }


}



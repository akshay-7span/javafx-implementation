package webcrawler.ui;

import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import webcrawler.crawller.Crawler;
import webcrawler.crawller.ImageCrawler;
import webcrawler.crawller.LinkCrawler;
import webcrawler.crawller.SEOAnalyzer;
import webcrawler.crawller.SEOCrawler;
import webcrawler.tables.DataTables;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static webcrawler.handler.EventHandlers.addBlockedForCrawlingProgressBar;
import static webcrawler.handler.EventHandlers.addProgressBar;
import static webcrawler.tables.TableCreation.createImageTable;
import static webcrawler.tables.TableCreation.createLinkDetailsTable;
import static webcrawler.tables.TableCreation.createSeoCrawlingTable;
import static webcrawler.ui.UIComponentFactory.backgroundFill;
import static webcrawler.ui.UIComponentFactory.crawlBudgetWasteLabel;
import static webcrawler.ui.UIComponentFactory.createBorderedVBox;
import static webcrawler.ui.UIComponentFactory.createLabelForTitle;
import static webcrawler.ui.UIComponentFactory.dropShadow;
import static webcrawler.ui.UIComponentFactory.gridPane;
import static webcrawler.ui.UIComponentFactory.hBox;
import static webcrawler.ui.UIComponentFactory.hBox1;
import static webcrawler.ui.UIComponentFactory.pieChart;
import static webcrawler.ui.UIComponentFactory.siteIndexabilityLabel;
import static webcrawler.ui.UIComponentFactory.startButton;
import static webcrawler.ui.UIComponentFactory.tabPane;
import static webcrawler.ui.UIComponentFactory.textField;
import static webcrawler.utils.CrawlerUtils.disableButtons;
import static webcrawler.utils.CrawlerUtils.displayGraph;
import static webcrawler.utils.CrawlerUtils.exportToExcel;
import static webcrawler.utils.DBUtils.fetchDataForPageName;
import static webcrawler.utils.DBUtils.fetchHeaderTagsFromDatabase;
import static webcrawler.utils.DBUtils.fetchImageDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchMetaDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchRepeatedWordsFromDatabase;
import static webcrawler.utils.DBUtils.getDistinctPageData;
import static webcrawler.utils.DBUtils.retrieveCrawledSeoDataFromDatabase;

public class DataHandler {

    private static final int NUM_THREADS = 10; // Number of threads for concurrent crawling tasks
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    public static final ProgressBar progressBar = new ProgressBar();

    public static Button exportButton = new Button();

    private EventHandler eventHandler;

    public static GridPane root() {

        TextField textField = textField();

        Button startButton = startButton();

        exportButton = new Button("Export Results");
        exportButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 20;");

        progressBar.setVisible(false);
        progressBar.setStyle("-fx-pref-width: 200px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;"); // Set width, height, and color


        HBox hbox = hBox(textField, startButton);

        Background vboxBackground = backgroundFill();

        // Apply CSS to the HBox
        hbox.getStyleClass().add("hbox");

        // Creating VBoxs
        VBox leftTopVBox = createBorderedVBox(1000, 300);
        VBox leftBottomVBox = createBorderedVBox(1000, 300);
        VBox rightTopVBox = createBorderedVBox(800, 550);
        VBox rightBottomVBox = createBorderedVBox(800, 550);

        // Applying background color and effects
        leftTopVBox.setStyle("-fx-background-color: #dcedc8; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        leftBottomVBox.setStyle("-fx-background-color: #dcedc8; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightTopVBox.setStyle("-fx-background-color: #dcedc8; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");
        rightBottomVBox.setStyle("-fx-background-color: #dcedc8; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);-fx-padding: 5;");

        PieChart pieChart = pieChart();

        Label titleLabel = createLabelForTitle();

        Label siteIndexabilityLabel = siteIndexabilityLabel();

        Label crawlBudgetWasteLabel = crawlBudgetWasteLabel();

        VBox vBoxForFirstRightTop = createBorderedVBox(400, 300);

        vBoxForFirstRightTop.getChildren().addAll(siteIndexabilityLabel, pieChart);
        VBox vBoxForFirstSecondTop = createBorderedVBox(400, 300);
        vBoxForFirstSecondTop.getChildren().add(crawlBudgetWasteLabel);

        vBoxForFirstRightTop.setStyle("-fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-background-color: #dcedc8;");
        vBoxForFirstSecondTop.setStyle("-fx-border-color: white; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-background-radius: 10px;-fx-background-color: #dcedc8;");


        // Create an HBox for the two VBox elements to be displayed side by side
        HBox hBox1 = hBox1();
        hBox1.getChildren().addAll(vBoxForFirstRightTop, vBoxForFirstSecondTop);

        // Add the HBox to the rightTopVBox
        rightTopVBox.getChildren().addAll(titleLabel, hBox1);


        TabPane tabPane = tabPane();

        DropShadow dropShadow = dropShadow();

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

        // Add the TableView to the leftTopVBox
        leftTopVBox.getChildren().add(createSeoCrawlingTable());



        GridPane root = gridPane(hbox, leftTopVBox, rightTopVBox, leftBottomVBox, rightBottomVBox);

        // Set TabPane to fill height and width
        tabPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        tabPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        tabPane.setMaxHeight(Double.MAX_VALUE);
        tabPane.setMaxWidth(Double.MAX_VALUE);






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



        // Start a background thread to continuously fetch data from the database
        startButton.setOnAction(event -> {
            String url = textField.getText().trim();
            if (!url.isEmpty()) {
                progressBar.setVisible(true);
                disableButtons(startButton);
                startCrawling(url, progressBar);

                // Start a background thread to continuously fetch data from the database
                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    try {
                        List<DataTables.SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                        Platform.runLater(() -> {
                            createSeoCrawlingTable().getItems().clear();
                            createSeoCrawlingTable().getItems().addAll(crawledSeoData);
                        });
                        Platform.runLater(() -> {
                            VBox rootForVbox = new VBox(10); // Vertical spacing between nodes
                            rootForVbox.setPadding(new Insets(20)); // Add some padding

                            addProgressBar(rootForVbox, "5xx error  ", "5%");
                            addProgressBar(rootForVbox, "4xx error  ", "4%");
                            addBlockedForCrawlingProgressBar(rootForVbox);

                            // Assuming you have a root node in your UI to add the VBox
                            // Replace "rootNode" with your actual root node reference
                            vBoxForFirstSecondTop.getChildren().clear();
                            vBoxForFirstSecondTop.getChildren().add(rootForVbox);
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, 0, 10, TimeUnit.SECONDS);
            }
        });

        // Start a background thread to continuously fetch data from the database
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                List<DataTables.SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                Platform.runLater(() -> {
                    createSeoCrawlingTable().getItems().clear();
                    createSeoCrawlingTable().getItems().addAll(crawledSeoData);
                });
                Platform.runLater(() -> {
                    VBox rootForVbox = new VBox(10); // Vertical spacing between nodes
                    rootForVbox.setPadding(new Insets(20)); // Add some padding

                    addProgressBar(rootForVbox, "5xx error  ", "5%");
                    addProgressBar(rootForVbox, "4xx error  ", "4%");
                    addBlockedForCrawlingProgressBar(rootForVbox);

                    // Assuming you have a root node in your UI to add the VBox
                    // Replace "rootNode" with your actual root node reference
                    vBoxForFirstSecondTop.getChildren().clear();
                    vBoxForFirstSecondTop.getChildren().add(rootForVbox);
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);



        // Event handler for URL column click

        createSeoCrawlingTable().setOnMouseClicked(event -> {
            if (!progressBar.isVisible()) {
                if (event.getClickCount() == 1 && !createSeoCrawlingTable().getSelectionModel().isEmpty()) {
                    // Get the selected item
                    DataTables.SEODataForCrawling selectedItem = createSeoCrawlingTable().getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String selectedUrl = selectedItem.getPageUrl();
                        try {
                            // Fetch meta data based on the selected URL
                            List<String> metaDataList = fetchMetaDataFromDatabase(selectedUrl);
                            // Clear the existing items in the meta table
                            tableViewMeta.getItems().clear();
                            // Add the fetched data to the meta table
                            tableViewMeta.getItems().addAll(metaDataList);
                            // Set the content of the meta tab to the meta table
                            Tab metaTab = new Tab("metaTab");
                            metaTab.setContent(tableViewMeta);

                            // Fetch H1 (header_tags) data from seo_analysis table based on the selected URL
                            List<String> headerTagsList = fetchHeaderTagsFromDatabase(selectedUrl);
                            // Clear the existing items in the H1 (header_tags) table
                            tableViewH1.getItems().clear();
                            // Add the fetched H1 (header_tags) data to the H1 (header_tags) table
                            tableViewH1.getItems().addAll(headerTagsList);
                            // Set the content of the H1 (header_tags) tab to the H1 (header_tags) table
                            Tab h1Tab = new Tab("h1Tab");

                            h1Tab.setContent(tableViewH1);

                            // Fetch repeated words data from seo_analysis table based on the selected URL
                            List<String> repeatedWordsList = fetchRepeatedWordsFromDatabase(selectedUrl);
                            // Clear the existing items in the repeated words table
                            tableViewRepeatedWords.getItems().clear();
                            // Add the fetched repeated words data to the repeated words table
                            tableViewRepeatedWords.getItems().addAll(repeatedWordsList);
                            // Set the content of the repeated words tab to the repeated words table

                            Tab wordCountTab = new Tab("wordCountTab");

                            wordCountTab.setContent(tableViewRepeatedWords);

                            List<DataTables.ImageData> imageDataList = fetchImageDataFromDatabase(selectedUrl);
                            createImageTable().getItems().clear(); // Clear existing items
                            createImageTable().getItems().addAll(imageDataList); // Add fetched data to the table

                            Tab imageTab = new Tab("imageTab");
                            imageTab.setContent(createImageTable());

                            String url = selectedItem.getPageUrl();
                            VBox vBox = displayGraph(url);
                            rightBottomVBox.getChildren().clear();
                            rightBottomVBox.getChildren().add(vBox);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });


        // Add a listener to the selection model property of the TabPane
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().equals("Crawl Links")) {
                if (!progressBar.isVisible()) { // Check if progressBar is not visible
                    // Fetching distinct page data from the database
                    List<DataTables.PageData> distinctPageData = getDistinctPageData();

                    if (!createLinkDetailsTable().getItems().isEmpty()) {
                        createLinkDetailsTable().getItems().clear();
                    }
                    // Clear existing items in the tableViewLinks
                    createLinkDetailsTable().getItems().clear();

                    // Adding fetched data to the tableViewLinks
                    for (DataTables.PageData pageData : distinctPageData) {
                        DataTables.PageData data = new DataTables.PageData(pageData.getPageName());
                        createLinkDetailsTable().getItems().add(data);
                    }

                    // Set the content of the "Links" tab to the tableViewLinks
                    Tab links = new Tab("links");

                    links.setContent(createLinkDetailsTable());
                }
            }
        });

        createLinkDetailsTable().getSelectionModel().selectedItemProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage != null) {
                String selectedPageName = newPage.getPageName();
                try {
                    // Query the database using the selected page name
                    List<DataTables.PageData> pageDataList = fetchDataForPageName(selectedPageName);

                    // Clear existing items in the linkTableView
                    createLinkDetailsTable().getItems().clear();

                    // Add fetched data to the linkTableView
                    createLinkDetailsTable().getItems().addAll(pageDataList);
                    Tab links = new Tab("links");

                    links.setContent(createLinkDetailsTable());
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle database query errors
                }
            }
        });


        exportButton.setOnAction(event -> {
            try {
                exportToExcel(exportButton);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return root;
    }
    public static void startCrawling(String url, ProgressBar progressBar) {

        Crawler linkCrawler = new LinkCrawler(executor);
        Crawler seoCrawler = new SEOCrawler(new SEOAnalyzer(), executor);
        Crawler imageCrawler = new ImageCrawler(executor);

        executor.submit(() -> linkCrawler.crawl(url, progressBar));
        executor.submit(() -> seoCrawler.crawl(url, progressBar));
        executor.submit(() -> imageCrawler.crawl(url, progressBar));

    }

}

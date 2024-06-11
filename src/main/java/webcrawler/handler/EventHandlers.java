package webcrawler.handler;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import webcrawler.crawller.Crawler;
import webcrawler.crawller.ImageCrawler;
import webcrawler.crawller.LinkCrawler;
import webcrawler.crawller.SEOAnalyzer;
import webcrawler.crawller.SEOCrawler;
import webcrawler.tables.DataTables.*;

import static webcrawler.utils.DBUtils.fetchDataForPageName;
import static webcrawler.utils.DBUtils.fetchHeaderTagsFromDatabase;
import static webcrawler.utils.DBUtils.fetchImageDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchMetaDataFromDatabase;
import static webcrawler.utils.DBUtils.fetchRepeatedWordsFromDatabase;
import static webcrawler.utils.DBUtils.getDistinctPageData;
import static webcrawler.utils.DBUtils.retrieveCrawledSeoDataFromDatabase;
import static webcrawler.utils.DBUtils.updateBlockedForCrawlingProgressBars;
import static webcrawler.utils.DBUtils.updateProgressBars;


import javafx.scene.control.*;

public class EventHandlers {

    private static final int NUM_THREADS = 10; // Number of threads for concurrent crawling tasks
    private static final ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    private final TextField urlTextField;
    private final ProgressBar progressBar;
    private final Button startButton;
    private final TableView<SEODataForCrawling> tableViewSEOCrawlingDetails;
    private final VBox vBoxForFirstSecondTop;
    private final TabPane tabPane;
    private final Tab metaTab;
    private final Tab h1Tab;
    private final Tab wordCountTab;
    private final Tab links;
    private final TableView<String> tableViewMeta;
    private final TableView<String> tableViewH1;
    private final TableView<String> tableViewRepeatedWords;
    private final TableView<ImageData> imageTable;
    private final TableView<PageData> tableView;
    private final TableView<PageData> linkTableView;
    private final VBox rightBottomVBox;

    public EventHandlers(TextField urlTextField, ProgressBar progressBar, Button startButton,
                         TableView<SEODataForCrawling> tableViewSEOCrawlingDetails, VBox vBoxForFirstSecondTop,
                         TabPane tabPane, Tab metaTab, Tab h1Tab, Tab wordCountTab, Tab links,
                         TableView<String> tableViewMeta, TableView<String> tableViewH1,
                         TableView<String> tableViewRepeatedWords, TableView<ImageData> imageTable,
                         TableView<PageData> tableView, TableView<PageData> linkTableView, VBox rightBottomVBox) {
        this.urlTextField = urlTextField;
        this.progressBar = progressBar;
        this.startButton = startButton;
        this.tableViewSEOCrawlingDetails = tableViewSEOCrawlingDetails;
        this.vBoxForFirstSecondTop = vBoxForFirstSecondTop;
        this.tabPane = tabPane;
        this.metaTab = metaTab;
        this.h1Tab = h1Tab;
        this.wordCountTab = wordCountTab;
        this.links = links;
        this.tableViewMeta = tableViewMeta;
        this.tableViewH1 = tableViewH1;
        this.tableViewRepeatedWords = tableViewRepeatedWords;
        this.imageTable = imageTable;
        this.tableView = tableView;
        this.linkTableView = linkTableView;
        this.rightBottomVBox = rightBottomVBox;

        initialize();
    }

    public void initialize() {
        setupStartButtonAction();
        setupTableClickListener();
        setupTabSelectionListener();
        setupTableSelectionListener();
    }

    public void setupStartButtonAction() {
        startButton.setOnAction(event -> {
            String url = urlTextField.getText().trim();
            if (!url.isEmpty()) {
                progressBar.setVisible(true);
                disableButtons(startButton);
                startCrawling(url, progressBar);

                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    try {
                        List<SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                        Platform.runLater(() -> {
                            tableViewSEOCrawlingDetails.getItems().clear();
                            tableViewSEOCrawlingDetails.getItems().addAll(crawledSeoData);
                        });
                        Platform.runLater(() -> {
                            VBox rootForVbox = new VBox(10);
                            rootForVbox.setPadding(new Insets(20));

                            addProgressBar(rootForVbox, "5xx error  ", "5%");
                            addProgressBar(rootForVbox, "4xx error  ", "4%");
                            addBlockedForCrawlingProgressBar(rootForVbox);

                            vBoxForFirstSecondTop.getChildren().clear();
                            vBoxForFirstSecondTop.getChildren().add(rootForVbox);
                        });
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }, 0, 5, TimeUnit.SECONDS);
            }
        });

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            try {
                List<SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                Platform.runLater(() -> {
                    tableViewSEOCrawlingDetails.getItems().clear();
                    tableViewSEOCrawlingDetails.getItems().addAll(crawledSeoData);
                });
                Platform.runLater(() -> {
                    VBox rootForVbox = new VBox(10);
                    rootForVbox.setPadding(new Insets(20));

                    addProgressBar(rootForVbox, "5xx error  ", "5%");
                    addProgressBar(rootForVbox, "4xx error  ", "4%");
                    addBlockedForCrawlingProgressBar(rootForVbox);

                    vBoxForFirstSecondTop.getChildren().clear();
                    vBoxForFirstSecondTop.getChildren().add(rootForVbox);
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void setupTableClickListener() {
        tableViewSEOCrawlingDetails.setOnMouseClicked(event -> {
            if (!progressBar.isVisible()) {
                if (event.getClickCount() == 1 && !tableViewSEOCrawlingDetails.getSelectionModel().isEmpty()) {
                    SEODataForCrawling selectedItem = tableViewSEOCrawlingDetails.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String selectedUrl = selectedItem.getPageUrl();
                        try {
                            List<String> metaDataList = fetchMetaDataFromDatabase(selectedUrl);
                            tableViewMeta.getItems().clear();
                            tableViewMeta.getItems().addAll(metaDataList);
                            metaTab.setContent(tableViewMeta);

                            List<String> headerTagsList = fetchHeaderTagsFromDatabase(selectedUrl);
                            tableViewH1.getItems().clear();
                            tableViewH1.getItems().addAll(headerTagsList);
                            h1Tab.setContent(tableViewH1);

                            List<String> repeatedWordsList = fetchRepeatedWordsFromDatabase(selectedUrl);
                            tableViewRepeatedWords.getItems().clear();
                            tableViewRepeatedWords.getItems().addAll(repeatedWordsList);
                            wordCountTab.setContent(tableViewRepeatedWords);

                            List<ImageData> imageDataList = fetchImageDataFromDatabase(selectedUrl);
                            imageTable.getItems().clear();
                            imageTable.getItems().addAll(imageDataList);

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void setupTabSelectionListener() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().equals("Crawl Links")) {
                if (!progressBar.isVisible()) {
                    List<PageData> distinctPageData = getDistinctPageData();

                    if (!linkTableView.getItems().isEmpty()) {
                        linkTableView.getItems().clear();
                    }
                    tableView.getItems().clear();

                    for (PageData pageData : distinctPageData) {
                        PageData data = new PageData(pageData.getPageName());
                        tableView.getItems().add(data);
                    }

                    links.setContent(tableView);
                }
            }
        });
    }

    private void setupTableSelectionListener() {
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage != null) {
                String selectedPageName = newPage.getPageName();
                try {
                    List<PageData> pageDataList = fetchDataForPageName(selectedPageName);
                    linkTableView.getItems().clear();
                    linkTableView.getItems().addAll(pageDataList);
                    links.setContent(linkTableView);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void disableButtons(Button... buttons) {
        for (Button button : buttons) {
            button.setDisable(true);
        }
    }
    public void startCrawling(String url,ProgressBar progressBar) {

        Crawler linkCrawler = new LinkCrawler(executor);
        Crawler seoCrawler = new SEOCrawler(new SEOAnalyzer(), executor);
        Crawler imageCrawler = new ImageCrawler(executor);

        executor.submit(() -> linkCrawler.crawl(url, progressBar));
        executor.submit(() -> seoCrawler.crawl(url, progressBar));
        executor.submit(() -> imageCrawler.crawl(url, progressBar));
        
    }

    public static void addProgressBar(VBox root, String labelName, String statusPrefix) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        progressBar.setStyle("-fx-pref-width: 150px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;");
        Label label = new Label(labelName);
        Label countLabel = new Label();
        countLabel.setMinWidth(30);

        updateProgressBars(progressBar, countLabel, statusPrefix);

        HBox progressBarBox = new HBox(10);
        progressBarBox.getChildren().addAll(label, countLabel, progressBar);
        root.getChildren().add(progressBarBox);
    }

    public static void addBlockedForCrawlingProgressBar(VBox root) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        progressBar.setStyle("-fx-pref-width: 150px; -fx-pref-height: 20px; -fx-background-color: #d3d3d3; -fx-accent: #00ff00;");
        Label label = new Label("Blocked for\ncrawling");
        Label countLabel = new Label();
        countLabel.setMinWidth(24);

        updateBlockedForCrawlingProgressBars(progressBar, countLabel);

        HBox progressBarBox = new HBox(10);
        progressBarBox.getChildren().addAll(label, countLabel, progressBar);
        root.getChildren().add(progressBarBox);
    }
}

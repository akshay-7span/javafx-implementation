package webcrawler.handler;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import webcrawler.constant.Constants;
import webcrawler.crawller.Crawler;
import webcrawler.crawller.ImageCrawler;
import webcrawler.crawller.LinkCrawler;
import webcrawler.crawller.SEOAnalyzer;
import webcrawler.crawller.SEOCrawler;
import webcrawler.tables.DataTables;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static webcrawler.tables.TableCreation.createImageTable;
import static webcrawler.tables.TableCreation.createSeoCrawlingTable;
import static webcrawler.ui.DataHandler.executor;
import static webcrawler.utils.CrawlerUtils.disableButtons;
import static webcrawler.utils.CrawlerUtils.displayGraph;
import static webcrawler.utils.CrawlerUtils.exportToExcel;
import static webcrawler.utils.DataStore.*;

public class EventHandlers {

    public static void setStartButtonAction(Button startButton, TextField textField, ProgressBar progressBar, VBox vBoxForFirstSecondTop) {
        startButton.setOnAction(event -> {
            String url = textField.getText().trim();
            if (!url.isEmpty()) {
                progressBar.setVisible(true);
                disableButtons(startButton);
                startCrawling(url, progressBar);

                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(() -> {
                    List<DataTables.SEODataForCrawling> crawledSeoData = retrieveCrawledSeoDataFromDatabase();
                    Platform.runLater(() -> {
                        createSeoCrawlingTable().getItems().clear();
                        createSeoCrawlingTable().getItems().addAll(crawledSeoData);
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
                }, 0, 20, TimeUnit.SECONDS);
            }
        });
    }

    public static void setSeoCrawlingTableClickAction(TableView<DataTables.SEODataForCrawling> seoCrawlingTable, ProgressBar progressBar, TableView<String> tableViewMeta, TableView<String> tableViewH1, TableView<String> tableViewRepeatedWords, VBox rightBottomVBox) {
        seoCrawlingTable.setOnMouseClicked(event -> {
            if (!progressBar.isVisible()) {
                if (event.getClickCount() == 1 && !seoCrawlingTable.getSelectionModel().isEmpty()) {
                    DataTables.SEODataForCrawling selectedItem = seoCrawlingTable.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String selectedUrl = selectedItem.getPageUrl();
                        List<String> metaDataList = fetchMetaDataFromDatabase(selectedUrl);
                        tableViewMeta.getItems().clear();
                        tableViewMeta.getItems().addAll(metaDataList);

                        List<String> headerTagsList = fetchHeaderTagsFromDatabase(selectedUrl);
                        tableViewH1.getItems().clear();
                        tableViewH1.getItems().addAll(headerTagsList);

                        List<String> repeatedWordsList = fetchRepeatedWordsFromDatabase(selectedUrl);
                        tableViewRepeatedWords.getItems().clear();
                        tableViewRepeatedWords.getItems().addAll(repeatedWordsList);

                        List<DataTables.ImageData> imageDataList = fetchImageDataFromDatabase(selectedUrl);
                        createImageTable().getItems().clear();
                        createImageTable().getItems().addAll(imageDataList);

                        VBox vBox = displayGraph(selectedUrl);
                        rightBottomVBox.getChildren().clear();
                        rightBottomVBox.getChildren().add(vBox);

                    }
                }
            }
        });
    }

    public static void setTabPaneSelectionListener(TabPane tabPane, ProgressBar progressBar, TableView<DataTables.PageData> linkDetailsTable) {
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getText().equals(Constants.TAB_TEXT_CRAWL_LINKS)) {
                if (!progressBar.isVisible()) {
                    List<DataTables.PageData> distinctPageData = getDistinctPageData();
                    if (!linkDetailsTable.getItems().isEmpty()) {
                        linkDetailsTable.getItems().clear();
                    }
                    linkDetailsTable.getItems().addAll(distinctPageData);
                }
            }
        });
    }

    public static void setLinkDetailsTableSelectionListener(TableView<DataTables.PageData> linkDetailsTable) {
        linkDetailsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldPage, newPage) -> {
            if (newPage != null) {
                String selectedPageName = newPage.getPageName();
                List<DataTables.PageData> pageDataList = fetchDataForPageName(selectedPageName);
                linkDetailsTable.getItems().clear();
                linkDetailsTable.getItems().addAll(pageDataList);
            }
        });
    }

    public static void setExportButtonAction(Button exportButton) {
        exportButton.setOnAction(event -> {
            try {
                exportToExcel(exportButton);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void addProgressBar(VBox root, String labelName, String statusPrefix) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(200);
        progressBar.setStyle(Constants.PROGRESS_STYLE);
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
        progressBar.setStyle(Constants.PROGRESS_STYLE);
        Label label = new Label(Constants.LABEL_TEXT_BLOCKED);
        Label countLabel = new Label();
        countLabel.setMinWidth(24);

        updateBlockedForCrawlingProgressBars(progressBar, countLabel);

        HBox progressBarBox = new HBox(10);
        progressBarBox.getChildren().addAll(label, countLabel, progressBar);
        root.getChildren().add(progressBarBox);
    }
    public static void startCrawling(String url, ProgressBar progressBar) {

        Crawler linkCrawler = new LinkCrawler(executor);
        Crawler seoCrawler = new SEOCrawler(new SEOAnalyzer());
        Crawler imageCrawler = new ImageCrawler();

        executor.submit(() -> linkCrawler.crawl(url, progressBar));
        executor.submit(() -> seoCrawler.crawl(url, progressBar));
        executor.submit(() -> imageCrawler.crawl(url, progressBar));

    }
}
